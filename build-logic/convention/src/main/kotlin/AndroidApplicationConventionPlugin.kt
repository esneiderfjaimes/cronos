import com.android.build.api.dsl.ApplicationExtension
import com.nei.cronos.configureKotlinAndroid
import com.nei.cronos.libs
import com.nei.cronos.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply(libs pluginId "jetbrains-android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }

            /*
            extensions.configure<KotlinJvmOptions> {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
            */
        }
    }
}