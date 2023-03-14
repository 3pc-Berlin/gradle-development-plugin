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

**code location**: `dreipc.plugins.development.module.Testing`