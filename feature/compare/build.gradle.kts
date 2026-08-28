plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)

}

android {
    namespace = "com.imglmd.feature.compare"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(project(":feature:history"))
    implementation(project(":core:experiments"))
    implementation(project(":core:ui"))
    implementation(project(":feature:experiment"))

    implementation(libs.kotlinx.serialization)

    implementation(libs.vico.compose.m3)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.bundles.koin)
}