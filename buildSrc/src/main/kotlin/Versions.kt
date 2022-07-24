object Versions {
    const val minSdk = 21
    const val compileSdk = 32

    const val kotlin = "1.6.10"
    const val coroutines = "1.6.3"
    const val compose = "1.1.1"
    const val retrofit = "2.9.0"
    const val lifecycle = "2.5.0"
    const val room = "2.4.2"
    const val robolectric = "4.8.1"
    const val hilt = "2.40.5"
}

object Deps {
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val androidxCore = "androidx.core:core-ktx:1.8.0"
    const val appcompat = "androidx.appcompat:appcompat:1.4.2"
    const val androidxAnnotation = "androidx.annotation:annotation:1.4.0"
    const val material = "com.google.android.material:material:1.6.1"

    // Hilt
    const val hiltCore = "com.google.dagger:hilt-core:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"

    // Tests
    const val junit = "junit:junit:4.13.2"
    const val androidxTestCore = "androidx.test:core:1.4.0"
    const val androidxCoreTesting = "androidx.arch.core:core-testing:2.1.0"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val mockitoCore = "org.mockito:mockito-core:4.6.1"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:4.0.0"
}