import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
    id("com.google.devtools.ksp")
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use(::load)
    }
}

fun propertyOrDefault(propertyName: String, envName: String, defaultValue: String): String {
    return localProperties.getProperty(propertyName)
        ?.trim()
        ?.takeIf(String::isNotEmpty)
        ?: providers.gradleProperty(propertyName).orNull
            ?.trim()
            ?.takeIf(String::isNotEmpty)
        ?: System.getenv(envName)
            ?.trim()
            ?.takeIf(String::isNotEmpty)
        ?: defaultValue
}

fun String.asBuildConfigValue(): String = "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

val backendBaseUrl = propertyOrDefault(
    propertyName = "backend.baseUrl",
    envName = "BACKEND_BASE_URL",
    defaultValue = "http://94.183.187.242:8001/"
)


kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

android {
    namespace = "com.imglmd.physicsexps"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.imglmd.physicsexps"
        minSdk = 29
        targetSdk = 37
        versionCode = 7
        versionName = "1.2.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BACKEND_BASE_URL", backendBaseUrl.asBuildConfigValue())
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    androidResources {
        generateLocaleConfig=true
    }
}
androidComponents {
    onVariants { variant ->

        val versionName = variant.outputs
            .single()
            .versionName
            .orNull ?: "unknown"

        variant.outputs.forEach { output ->
            output.outputFileName.set(
                "PhysicsExps-v$versionName.apk"
            )
        }
    }
}

dependencies {
    implementation(project(":feature:settings"))
    implementation(project(":feature:constants"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:experiments"))

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Icons
    implementation(libs.androidx.compose.material.icons.core)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // Navigation + DI
    implementation(libs.bundles.nav3)
    implementation(libs.bundles.koin)

    // Serialization
    implementation(libs.kotlinx.serialization)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Charts (Vico)
    implementation(libs.vico.compose.m3)

    // Latex
    implementation(libs.latex.renderer)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.coil.compose)
}
