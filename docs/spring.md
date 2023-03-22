## Gradle Property Expansion | Spring
This plugin reacts on all spring application files (application*.yml | application*.properties) and enables property expansion.
Meaning that all gradle based properties (`gradlew properties`) can injected into spring settings files by the  @...@ annotation. If you like to add the gradle project name as spring application name you can do that by using this feature:

#### Example: Injecting gradle name property:
```yaml
spring:
  application:
    name: @name@
    version: @version@
...
```

**code location**: `dreipc.plugins.development.module.GradlePropertyExpansion`

---

## Automated Spring Test Dependencies
If your project enables the gradle plugin `org.springframework.boot`, this plugin injects the spring test dependency `org.springframework.boot:spring-boot-starter-test`.
Additionally if the Project uses `spring-boot-starter-webflux` or `reactor:core` as implementation Dependency, this plugin injects the test dependencies: `io.projectreactor:reactor-test` and `io.projectreactor.tools:blockhound`.

**code location**: `dreipc.plugins.development.module.Testing`

---
## Auto-enabled Docker Gradle Plugin
If your project enables the gradle plugin `org.springframework.boot`, this plugin enables the Gradle Docker Plugin to generate Docker Images out of the Spring boot Application.

**code location**: `dreipc.plugins.development.DevelopmentPlugin`