import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "net.cacheux.dummyapi"
        minSdk = Versions.minSdk
        targetSdk = Versions.compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        all {
            buildConfigField("String", "API_URL", "\"https://dummyapi.io/data/v1/\"")
            buildConfigField("String", "API_KEY", "\"${gradleLocalProperties(rootDir).getProperty("api_key")}\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion  = Versions.compose
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":datasource-cached"))
    implementation(project(":datasource-room"))
    implementation(project(":datasource-server"))

    implementation(Deps.androidxCore)
    implementation(Deps.appcompat)
    implementation(Deps.material)
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.paging:paging-compose:1.0.0-alpha15")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}")

    implementation("androidx.activity:activity-compose:1.5.0")
    implementation("io.coil-kt:coil-compose:1.4.0")

    implementation(Deps.hiltAndroid)
    kapt(Deps.hiltAndroidCompiler)

    testImplementation(project(":datasource-test"))
    testImplementation(project(":datasource-memory"))
    testImplementation(Deps.junit)
    testImplementation(Deps.androidxTestCore)
    testImplementation(Deps.androidxCoreTesting)
    testImplementation(Deps.coroutinesTest)
    testImplementation("org.robolectric:robolectric:${Versions.robolectric}")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")

    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
}

kapt {
    correctErrorTypes = true
}
