plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.ksp)

}

android {
    namespace = "com.hye.healthpossible"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.hye.healthpossible"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":domain"))
    implementation(project(":features"))
    implementation(project(":features:auth"))
    implementation(project(":features:community"))
    implementation(project(":features:mission"))
    implementation(project(":features:mypage"))
    implementation(project(":shared"))
    implementation(project(":data"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.material)

    implementation(libs.bundles.compose.libs)
    implementation(libs.kotlinx.serialization.json)

    // Hilt Core
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.foundation)
    ksp(libs.hilt.compiler)
    // Hilt Navigation (ViewModel 주입에 필요)
    implementation(libs.hilt.navigation.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase.libraries)

    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}