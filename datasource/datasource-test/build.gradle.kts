plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":common"))
    implementation(Deps.coroutinesCore)
    implementation(Deps.koinCore)

    testImplementation(Deps.junit)
}