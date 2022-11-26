import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test


class KtorTest {

    @Test
    fun userByUsername() {
        val apiClient = mockEngine(getResource("UserByUsername.json"))
        // TODO()
    }


    @Test
    fun userLikedTweets() {
        val apiClient = mockEngine(getResource("UserLikedTweets.json"))
        // TODO()
    }

    private fun mockEngine(response: String): ApiClient =
        runBlocking {
            val mockEngine = MockEngine { request ->
                respond(
                    content = ByteReadChannel(response),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            return@runBlocking ApiClient(engine = mockEngine)
        }
}
