import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginDemo implements Plugin<Project>{

    @Override
    void apply(Project target) {
        println "hello plugin:" + target.name
    }
}