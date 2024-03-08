import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.nei.cronos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nei.cronos"
        minSdk = 30
        targetSdk = 34
        versionCode = 2
        versionName = "0.1.0-beta2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    sourceSets {
        val androidTestAssets = sourceSets.getByName("androidTest").assets
        androidTestAssets.srcDirs(files("$projectDir/schemas"))
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        tasks.withType(KotlinCompile::class.java).configureEach {
            kotlinOptions.jvmTarget = "17"
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(project(":core:model"))
    implementation(project(":core:database"))

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.ui)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Dagger
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Room
    implementation(libs.bundles.room)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)

    // Preferences
    implementation(libs.datastore.preferences)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}