import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import twitterapi.LikeResponse
import twitterapi.UserResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class TwitterEntitiesSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun userByUsername() {
        with(json.decodeFromString<UserResponse>(getResource("UserByUsername.json")))
        {
            assertEquals(123456789UL, data.id)
            assertEquals("I R I S", data.name)
            assertEquals("Iris", data.username)
        }
    }


    @Test
    fun userLikedTweets() {
        with(json.decodeFromString<LikeResponse>(getResource("UserLikedTweets.json")))
        {
            assertEquals(10, data.size)
            assertEquals(13, includes.media!!.size)
        }
    }
}
