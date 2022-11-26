package twitterapi

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// The data structures covered here are non-exhausted
// Some fields may be missing or specific to this application

// Response from
// https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-by-username-username
// GET /2/users/by/username/:username
@Serializable
data class UserResponse(
    @SerialName("data") val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("id") @Serializable(with = ULongAsStringSerializer::class) val id: ULong,
        @SerialName("name") val name: String,
        @SerialName("username") val username: String
    )
}

// Response from
// https://developer.twitter.com/en/docs/twitter-api/tweets/likes/api-reference/get-users-id-liked_tweets
// GET /2/users/:id/liked_tweets
@Serializable
data class LikeResponse(
    @SerialName("data") val data: List<TweetData>,
    @SerialName("includes") val includes: Includes,
) {
    @Serializable
    data class Includes(
        @SerialName("media") val media: List<MediaData>? = null,
        @SerialName("users") val users: List<UserData>? = null
    )
}

@Serializable
data class MediaData(
    @SerialName("media_key") val mediaKey: String,
    @SerialName("type") val type: MediaTypes,
    @SerialName("url") val url: String? = null,
    @SerialName("variants") val variants: List<Variant>? = null,
    @SerialName("alt_text") val altText: String? = null
) {
    @Serializable
    enum class MediaTypes {
        @SerialName("photo") PHOTO,
        @SerialName("video") VIDEO,
        @SerialName("animated_gif") ANIMATED_GIF
    }

    @Serializable
    data class Variant(
        @SerialName("bit_rate") val bitRate: Int? = null,
        @SerialName("content_type") val contentType: String,
        @SerialName("url") val url: String
    )
}

@Serializable
data class TweetData(
    @SerialName("id") @Serializable(with = ULongAsStringSerializer::class) val id: ULong,
    @SerialName("text") val text: String,
    @SerialName("created_at") val createdAt: Instant? = null,
    @SerialName("author_id") @Serializable(with = ULongAsStringSerializer::class) val authorId: ULong? = null,
    @SerialName("attachments") val attachments: Attachments? = null
) {
    @Serializable
    data class Attachments(
        @SerialName("media_keys") @Serializable val mediaKeys: List<String>? = null
    )
}

@Serializable
data class UserData(
    @SerialName("id") val id: ULong,
    @SerialName("name") val name: String? = null,
    @SerialName("username") val username: String? = null
)
