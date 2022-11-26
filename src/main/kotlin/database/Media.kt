package database

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import twitterapi.MediaData


object H2MediaTable : IdTable<String>("media") {
    override val id = text("id").uniqueIndex().entityId() // mediaKey
    val type = enumeration<MediaData.MediaTypes>("text")
    val url = text("url").nullable()
    val altText = text("altText").nullable()
}

class H2MediaEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, H2MediaEntity>(H2MediaTable) {
        fun new(media: MediaData) = new(media.mediaKey) {
            updateFromMediaData(this, media)
        }

        // Update this database entity with values from the MediaData model
        fun updateFromMediaData(mediaEntity: H2MediaEntity, media: MediaData): H2MediaEntity = run {
            with(mediaEntity) {
                type = media.type
                url = when (type) {
                    MediaData.MediaTypes.PHOTO -> media.url
                    // select the variant with the highest bitrate
                    MediaData.MediaTypes.VIDEO, MediaData.MediaTypes.ANIMATED_GIF -> media.variants?.maxBy {
                        it.bitRate ?: -1
                    }?.url
                }

                altText = media.altText
            }
            return mediaEntity
        }
    }

    var type by H2MediaTable.type
    var url by H2MediaTable.url
    var altText by H2MediaTable.altText

    fun toModel() = MediaData(id.value, type, url, altText = altText)
}
