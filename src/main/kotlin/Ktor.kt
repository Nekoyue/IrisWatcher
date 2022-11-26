import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient(
    defaultRequestUrl: String? = null,
    auth: Auth.() -> Unit = {}, // see twitterApiClient for usage
    engine: HttpClientEngine = CIO.create {
        Env.HTTP_PROXY?.let { proxy = ProxyBuilder.http(it) }
    } // Using CIO engine by default with optional http proxy
) {
    val httpClient = HttpClient(engine) {
        expectSuccess = true

        defaultRequest { defaultRequestUrl?.let { url(it) } }
        install(Auth) { auth() }

        install(Resources) // for creating serializable requests
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
            socketTimeoutMillis = 10000
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    klogger.debug { "HTTP ApiClient - $message" }
                }
            }
            level = LogLevel.INFO
        }
    }
}
