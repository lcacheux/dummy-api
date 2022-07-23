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
    implementation(Deps.androidxAnnotation)

    testImplementation(project(":datasource-test"))
    testImplementation(Deps.junit)
    testImplementation(Deps.androidxCoreTesting)
}