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

    testImplementation(project(":datasource-test"))
    testImplementation(project(":datasource-memory"))
    testImplementation(Deps.junit)
    testImplementation(Deps.androidxCoreTesting)
    testImplementation(Deps.mockitoCore)
    testImplementation(Deps.mockitoKotlin)
}