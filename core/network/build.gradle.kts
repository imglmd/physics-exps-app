plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.imglmd.physicsexps.core.network"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation(libs.koin.android)
}