import java.util.Properties

val properties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}
val kdcaToken = properties.getProperty("KDCA_API_TOKEN") ?: ""

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.hilt.gradle)
    kotlin("kapt")
}

android {
    namespace = "com.hye.data"
    compileSdk {
        version = release(36)
    }
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "KDCA_API_TOKEN", "\"$kdcaToken\"")
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
    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.google.firebase.vertexai)

    // Hilt Core
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    //xml
    implementation(libs.bundles.tik.xml)
    kapt(libs.tikxml.processor)
    implementation(libs.logging.interceptor)

    implementation(libs.org.json)

    implementation(libs.bundles.network)

    implementation(libs.bundles.room)

    implementation(libs.timber)

    //test
    testImplementation(libs.bundles.test.jvm)
    androidTestImplementation(libs.bundles.test.android)
}