package dreipc.gradle.common.plugins.gradlepluginexample

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

import static org.junit.jupiter.api.Assertions.assertNotNull

@ExtendWith(SpringExtension.class)
class GradlePluginExampleApplicationTests {
	@Test
	 void greeterPluginAddsGreetingTaskToProject() {
		Project project = ProjectBuilder.builder().build()
		project.pluginManager.apply 'dreipc.gradle.spring'
		System.out.println(project.getTasks().getByName("hello"))
		assertNotNull(project.tasks.hello)

	}
}
