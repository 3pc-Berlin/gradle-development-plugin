package dreipc.plugins.development.extension

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

open class DockerPluginExtension(objects: ObjectFactory) {
  val image: Property<String> = objects.property(String::class.java)
}
