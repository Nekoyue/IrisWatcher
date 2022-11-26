import database.db
import eagleapi.EagleItem
import eagleapi.eagleApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import twitterapi.TweetData
import twitterapi.twitterApi

val klogger = KotlinLogging.logger {}.also {
    // set the log4j logger's level to INFO (ERROR by default)
    // Change to DEBUG for more verbose information
    val context = LogManager.getContext(false) as LoggerContext
    val rootConfig = context.configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
    rootConfig.level = Level.INFO
    context.updateLoggers()
}

fun main() = runBlocking {
    // The user we are going to interact with
    val irisUser = twitterApi.findUserByUsername(Env.IRIS_USERNAME).data

    while (true) {
        klogger.info { "Pulling updates from twitter" }
        twitterApi.likedTweets(irisUser.id).run {
            // Twitter separate the responses into two parts:
            // tweets (data) and the entities in tweets (includes.media, includes.users)
            includes.media?.forEach { media ->
                val query = db.queryMedia(media.mediaKey)
                if (query != null) return@forEach
                klogger.info { "New media ${media.type} with key ${media.mediaKey} and url ${media.url} found" }
                db.insertMedia(media)
            }

            includes.users?.forEach { user ->
                val query = db.queryUser(user.id)
                if (query != null) return@forEach
                klogger.info { "New user ${user.name} (@${user.username}) found" }
                db.insertUser(user)
            }

            // the data will be processed in batch at the end
            val eagleRequestQueue = mutableListOf<EagleItem>()
            val dbQueue = mutableListOf<TweetData>()
            data.forEach { tweet ->
                val query = db.queryTweet(tweet.id)
                if (query != null) return@forEach
                klogger.info { "New liked tweet created by ${tweet.authorId} with id ${tweet.id} found" }
                // create eagleapi object(s)
                // note: a tweet may contain multiple media entities
                tweet.attachments?.mediaKeys?.forEach mediaKeys@{ mediaKey ->
                    val media = db.getMedia(mediaKey)
                    val author = tweet.authorId?.let { authorId -> db.getUser(authorId) }
                    if (media?.url == null) return@mediaKeys // no images/videos in this tweet
                    eagleRequestQueue.add(
                        EagleItem(
                            url = media.url,
                            name = tweet.text.replace(Regex("https://t.co/\\S*$"), ""),
                            website = "https://twitter.com/${author?.username ?: tweet.authorId}/status/${tweet.id}",
                            tags = author?.username?.let { listOf("@$it") },
                            annotation = "${author?.name} (@${author?.username})\n${tweet.text} ${media.altText ?: ""}".trim(),
                            modificationTime = tweet.createdAt?.toEpochMilliseconds()
                        )
                    )
                }
                dbQueue.add(tweet)
            }

            eagleApi.addMediaFromUrls(eagleRequestQueue)
            dbQueue.forEach { db.insertTweet(it) }
        }

        // check for updates every 5 minutes (by default)
        klogger.info { "Done. Rerun the task in ${Env.CHECK_INTERVAL} minutes" }
        delay((Env.CHECK_INTERVAL) * 60 * 1000)
    }
}

