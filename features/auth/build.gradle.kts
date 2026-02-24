plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.hye.features.auth"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
}
kotlin{
    jvmToolchain(21)
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":domain"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.android.core)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coil)

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.timber)
}