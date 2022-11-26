package database

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import twitterapi.UserData


object H2UserTable : IdTable<ULong>("twitter_users") {
    override val id = ulong("id").uniqueIndex().entityId()
    val name = text("name").nullable()
    val username = text("username").nullable()
}

class H2UserEntity(id: EntityID<ULong>) : Entity<ULong>(id) {
    companion object : EntityClass<ULong, H2UserEntity>(H2UserTable) {
        fun new(user: UserData) = new(user.id) {
            updateFromUserData(this, user)
        }

        // Update this database entity with values from the MediaData model
        fun updateFromUserData(userEntity: H2UserEntity, user: UserData): H2UserEntity = run {
            with(userEntity) {
                name = user.name
                username = user.username
            }
            return userEntity
        }
    }

    var name by H2UserTable.name
    var username by H2UserTable.username

    fun toModel() = UserData(id.value, name, username)
}

