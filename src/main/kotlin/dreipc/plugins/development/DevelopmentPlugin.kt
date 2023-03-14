package dreipc.plugins.development

import dreipc.plugins.development.modul.Lombok
import dreipc.plugins.development.modul.GradlePropertyExpansion
import dreipc.plugins.development.modul.SemanticVersioning
import dreipc.plugins.development.modul.Testing
import org.gradle.api.Plugin
import org.gradle.api.Project


class DevelopmentPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        if (!target.plugins.hasPlugin("java")) {
            target.plugins.apply("java")
        }

        target.plugins.apply(Lombok::class.java)
        target.plugins.apply(GradlePropertyExpansion::class.java)
        target.plugins.apply(Testing::class.java)
        target.plugins.apply(SemanticVersioning::class.java)
    }
}