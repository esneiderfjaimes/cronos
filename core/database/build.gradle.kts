plugins {
    id("cronos.android.library")
    id("cronos.android.hilt")
    id("cronos.android.room")
}

android {
    namespace = "cronos.core.database"

    defaultConfig {
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

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(project(":core:model"))

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}