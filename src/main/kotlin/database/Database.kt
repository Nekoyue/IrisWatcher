package database

import klogger
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.transaction
import twitterapi.MediaData
import twitterapi.TweetData
import twitterapi.UserData

val db = H2TweetDB()


class H2TweetDB {
    private val dbPath = with(System.getProperty("os.name")) {
        when {
            this == "Mac OS X" -> "~/Library/Application Support/iriswatcher/iriswatcher"
            startsWith("Win") -> "${System.getenv("APPDATA")}\\iriswatcher\\iriswatcher"
            startsWith("Linux") -> "/opt/iriswatcher/iriswatcher"
            else -> throw Error("Unsupported OS")
        }
    }

    private val db = Database.connect(
        "jdbc:h2:file:$dbPath",
        driver = "org.h2.Driver",
        databaseConfig = DatabaseConfig.invoke {
            sqlLogger = object : SqlLogger {
                override fun log(context: StatementContext, transaction: Transaction) {
                    klogger.debug { "SQL - ${context.expandArgs(transaction)}" }
                }
            }
        }
    )

    init {
        transaction(db) {
            SchemaUtils.create(H2LikedHistoryTable)
            SchemaUtils.create(H2MediaTable)
            SchemaUtils.create(H2UserTable)
        }
    }

    /* TweetData */
    // returns a TweetData data class
    fun getTweet(entity: H2TweetEntity): TweetData = entity.toModel()
    fun getTweet(id: ULong): TweetData? = queryTweet(id)?.toModel()

    // returns a DAO entity which can be operated with
    fun queryTweet(id: ULong): H2TweetEntity? = transaction { H2TweetEntity.findById(id) }

    fun insertTweet(tweet: TweetData): H2TweetEntity = transaction { H2TweetEntity.new(tweet) }

    private fun updateTweet(tweet: TweetData): H2TweetEntity? = transaction {
        H2TweetEntity.findById(tweet.id)?.let { updateTweet(it, tweet) }
    }

    private fun updateTweet(tweetEntity: H2TweetEntity, tweet: TweetData): H2TweetEntity = transaction {
        H2TweetEntity.updateFromTweetData(tweetEntity, tweet)
    }

    /* MediaData */
    // returns a MediaData data class
    fun getMedia(entity: H2MediaEntity): MediaData = entity.toModel()
    fun getMedia(mediaKey: String): MediaData? = queryMedia(mediaKey)?.toModel()

    // returns a DAO entity which can be operated with
    fun queryMedia(mediaKey: String): H2MediaEntity? = transaction { H2MediaEntity.findById(mediaKey) }

    fun insertMedia(media: MediaData): H2MediaEntity = transaction { H2MediaEntity.new(media) }

    /* UserData */
    fun getUser(entity: H2UserEntity): UserData = entity.toModel()
    fun getUser(id: ULong): UserData? = queryUser(id)?.toModel()

    // returns a DAO entity which can be operated with
    fun queryUser(id: ULong): H2UserEntity? = transaction { H2UserEntity.findById(id) }

    fun insertUser(user: UserData): H2UserEntity = transaction { H2UserEntity.new(user) }
}

