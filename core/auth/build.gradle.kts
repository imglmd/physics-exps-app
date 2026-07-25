plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.imglmd.core.auth"
    compileSdk {
        version = release(37)
    }

    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.kotlinx.serialization)

    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
}