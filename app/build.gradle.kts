import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.android.application)
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.jetbrains.compose)
}

android {
    namespace = "net.cacheux.dummyapi"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "net.cacheux.dummyapi"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.compileSdk.get().toInt()
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
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
    namespace = "net.cacheux.dummyapi"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":datasource-cached"))
    implementation(project(":datasource-room"))
    implementation(project(":datasource-server"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.material)

    implementation(compose.ui)
    implementation(compose.material3)
    implementation(compose.runtime)
    implementation(compose.uiTooling)

    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(project(":datasource-test"))
    testImplementation(project(":datasource-memory"))
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    @OptIn(ExperimentalComposeLibrary::class)
    androidTestImplementation(compose.uiTest)

    debugImplementation(compose.uiTooling)
}

kapt {
    correctErrorTypes = true
}
