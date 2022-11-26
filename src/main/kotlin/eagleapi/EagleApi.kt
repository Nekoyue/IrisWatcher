package eagleapi

import ApiClient
import Env
import io.ktor.client.request.*
import io.ktor.http.*

val eagleApi = EagleApi()

class EagleApi {
    private val eagleApiClient = ApiClient(
        defaultRequestUrl = Env.EAGLE_URL
    ).httpClient

    // https://api.eagle.cool/item/add-from-url
    suspend fun addMediaFromUrls(items: List<EagleItem>) {
        // Ktor's @Resource annotation do not support Json body type
        // doing this manually for now
        eagleApiClient.post("/api/item/addFromURLs") {
            contentType(ContentType.Application.Json)
            setBody(AddFromURLsRequest(items))
        }
    }
}

