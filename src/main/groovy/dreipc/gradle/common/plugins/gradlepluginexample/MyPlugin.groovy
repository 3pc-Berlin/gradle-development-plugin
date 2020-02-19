package dreipc.gradle.common.plugins.gradlepluginexample

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.springframework.stereotype.Component

@Component
class GreetingPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('hello') {
            doLast {
                println 'Hello from the GreetingPlugin'
            }
        }

        project.task('springDefault') {
            doLast {
                println 'Hello from the GreetingPlugin'
            }
        }
    }
}

// Apply the plugin
apply plugin: GreetingPlugin

