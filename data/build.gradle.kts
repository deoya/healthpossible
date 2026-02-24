plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.hilt.gradle)
}

android {
    namespace = "com.hye.data"
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

}
kotlin{
    jvmToolchain(21)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":shared"))

    implementation(libs.bundles.android.core)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Hilt Core
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.timber)
}