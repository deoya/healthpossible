plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.hye.features"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24
    }
}
kotlin{
    jvmToolchain(21)
}