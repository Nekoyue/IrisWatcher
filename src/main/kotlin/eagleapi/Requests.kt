package eagleapi

import kotlinx.serialization.Serializable

@Serializable
data class AddFromURLsRequest(
    val items: List<EagleItem>,
    val folderId: String? = null
)

@Serializable
data class EagleItem(
    val url: String,
    val name: String,
    val website: String? = null,
    val tags: List<String>? = null,
    val annotation: String? = null,
    val modificationTime: Long? = null // in milliseconds
)
