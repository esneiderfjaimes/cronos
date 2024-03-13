plugins {
    id("cronos.android.library")
    id("cronos.android.hilt")
}

android {
    namespace = "cronos.core.datastore"

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(project(":core:model"))

    // Preferences
    implementation(libs.datastore.preferences)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}