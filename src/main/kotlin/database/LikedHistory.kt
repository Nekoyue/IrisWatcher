package database

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import twitterapi.TweetData


object H2LikedHistoryTable : IdTable<ULong>("liked_history") {
    override val id = ulong("id").uniqueIndex().entityId()
    val text = text("text")
    val createdAt = timestamp("created_at").nullable()
    val authorId = ulong("author_id").nullable()
    val mediaKeys = text("media_keys").nullable()
}

// DAO Entity of each row
class H2TweetEntity(id: EntityID<ULong>) : Entity<ULong>(id) {
    companion object : EntityClass<ULong, H2TweetEntity>(H2LikedHistoryTable) {
        fun new(tweet: TweetData) = new(tweet.id) {
            updateFromTweetData(this, tweet)
        }

        // Update this database entity with values from the MediaData model
        fun updateFromTweetData(tweetEntity: H2TweetEntity, tweet: TweetData): H2TweetEntity = run {
            with(tweetEntity) {
                text = tweet.text
                createdAt = tweet.createdAt
                authorId = tweet.authorId
                mediaKeys = tweet.attachments?.mediaKeys?.run { Json.encodeToString(this) }
            }
            return tweetEntity
        }
    }

    var text by H2LikedHistoryTable.text
    var createdAt by H2LikedHistoryTable.createdAt
    var authorId by H2LikedHistoryTable.authorId
    var mediaKeys by H2LikedHistoryTable.mediaKeys

    fun toModel() = TweetData(id.value, text, createdAt, authorId, mediaKeys?.run { Json.decodeFromString(this) })
}
