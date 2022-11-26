// Configurable environment variables
// TODO: Replace with config files
class Env {
    companion object {
        val IRIS_USERNAME = System.getenv("IRIS_USERNAME") ?: throw Error("IRIS_USERNAME not found")
        val BEARER_TOKEN = System.getenv("BEARER_TOKEN") ?: throw Error("BEARER_TOKEN not found")
        val HTTP_PROXY: String? = System.getenv("http_proxy") ?: System.getenv("HTTP_PROXY")
        val EAGLE_URL: String = System.getenv("EAGLE_URL") ?: "http://localhost:41595/"
        val LIMIT: Int = System.getenv("LIMIT")?.toInt() ?: 15
        val CHECK_INTERVAL: Long = System.getenv("CHECK_INTERVAL")?.toLong() ?: 5
    }
}
