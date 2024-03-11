import com.android.build.gradle.LibraryExtension
import com.nei.cronos.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            val extension: LibraryExtension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}
