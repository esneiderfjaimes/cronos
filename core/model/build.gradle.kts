plugins {
    id("cronos.android.library")
}

android {
    namespace = "cronos.core.model"

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.core.ktx)
}