plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("plugin.serialization") version Versions.kotlin
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":common"))

    implementation(Deps.coroutinesCore)
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    implementation(Deps.koinCore)

    testImplementation(Deps.junit)
    testImplementation(Deps.coroutinesTest)
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation(Deps.mockitoCore)
    testImplementation(Deps.mockitoKotlin)
}