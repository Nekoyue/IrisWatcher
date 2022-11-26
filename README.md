# IrisWatcher

Exporting images in Liked Tweets to a local Eagle.cool library.

This demo project uses Ktor HTTP client with Exposed SQL framework.

### Setup

Run `./gradlew buildFatJar` to build the executable file. The built artifact will locate
at `build/libs/IrisWatcher-all.jar`.

Note: Require Java 11+ to compile and run.

### Run

#### Set the environment variables

`IRIS_USERNAME` - Username to pull liked tweets from, do not include `@`.

`BEARER_TOKEN` - API token for Twitter. It can be obtained from
the [Twitter Developer Portal](https://developer.twitter.com/en/portal/dashboard).

`HTTP_PROXY` (optional) - HTTP proxy for all the requests. No proxy will be used if not set.

`EAGLE_URL` (optional) - Override the default URL for the local Eagle server.

`LIMIT` (optional) - Number of tweets to pull. Value should be in range 10..100. (15 by default)

`CHECK_INTERVAL` (optional) - Minutes to delay before checking updates again. (5 minutes by default)

#### Run the application

Open the Eagle app first before running this application

```
java -jar build/libs/IrisWatcher-all.jar
```

---
An H2 database file will be created to track processed tweets under:

- macOS: `~/Library/Application Support/iriswatcher/`
- Windows: `%APPDATA%\iriswatcher\`
- Linux: `/opt/iriswatcher/`

### Current limitations & TODOs

- Only the last 100 liked tweets can be exported
- Add support for configuration files (e.g., toml)
