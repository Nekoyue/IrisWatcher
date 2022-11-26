package twitterapi

import ApiClient
import Env
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.resources.*

val twitterApi = TwitterApi()

class TwitterApi {
    private val twitterApiClient = ApiClient(defaultRequestUrl = "https://api.twitter.com/",
        auth = {
            bearer { loadTokens { BearerTokens(Env.BEARER_TOKEN, "") } }
        }).httpClient

    // Likes
    // https://developer.twitter.com/en/docs/twitter-api/tweets/likes/api-reference/get-users-id-liked_tweets
    // GET /2/users/:id/liked_tweets
    suspend fun likedTweets(userId: ULong): LikeResponse {
        val expansions = setOf("attachments.media_keys", "author_id")
        val tweetFields = setOf("id", "text", "created_at", "author_id", "attachments")
        val mediaFields = setOf("media_key", "type", "url", "variants", "alt_text")
        val userFields = setOf("name", "username")
        return twitterApiClient.get(
            LikedTweetsRequest(
                userId, Env.LIMIT,
                expansions, tweetFields, mediaFields, userFields
            )
        ).body()
    }

    // Users lookup
    // https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-by-username-username
    // GET /2/users/by/username/:username
    suspend fun findUserByUsername(username: String): UserResponse =
        twitterApiClient.get(FindUserByUsernameRequest(username)).body()

}
