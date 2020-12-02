import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * @author ChaiHongwei
 * @date  2020-11-26 15:43
 */
class MyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println(project.name)
    }
}