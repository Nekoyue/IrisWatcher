package twitterapi

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Resource("/2/users/{id}/liked_tweets")
data class LikedTweetsRequest(
    val id: ULong,
    @SerialName("max_results") val maxResults: Int? = null, // between 10..100
    val expansions: String? = null,
    @SerialName("tweet.fields") val tweetFields: String? = null,
    @SerialName("media.fields") val mediaFields: String? = null,
    @SerialName("user.fields") val userField: String? = null
) {
    constructor(
        id: ULong, maxResults: Int?,
        expansions: Set<String>? = null, tweetFields: Set<String>? = null,
        mediaFields: Set<String>? = null, userField: Set<String>? = null
    ) : this(
        id, maxResults,
        expansions?.joinToString(","), tweetFields?.joinToString(","),
        mediaFields?.joinToString(","), userField?.joinToString(",")
    )
}


@Serializable
@Resource("/2/users/by/username/{username}")
data class FindUserByUsernameRequest(
    val username: String
)
