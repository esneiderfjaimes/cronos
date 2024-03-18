plugins {
    id("cronos.android.library")
    id("cronos.android.hilt")
}

android {
    namespace = "cronos.core.data"

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.core.ktx)

    // project
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}