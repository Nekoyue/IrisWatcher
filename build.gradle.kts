import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
    application
}

group = "yue.moe"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}


dependencies {
    val coroutinesVersion: String by project
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    val serializationVersion: String by project
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    val datetimeVersion: String by project
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    val loggingVersion: String by project
    implementation("io.github.microutils:kotlin-logging:$loggingVersion")
    val log4jVersion: String by project
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")

    // Network
    val ktorVersion: String by project
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-resources:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    // Database
    val exposedVersion: String by project
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    val h2Version: String by project
    implementation("com.h2database:h2:$h2Version")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}
