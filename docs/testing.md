## Test Setup
This Plugin pre-defines three tasks for testing including the setup for logging, parallel-processing and additional dependencies enabled by context.
If the spring Boot Plugin (`org.springframework.boot`) is enabled, the `spring-boot-starter-test` dependency is added to the test source set .
if the `spring-boot-webflux` dependency is detected, the `io.projectreactor:reactor-test` and `io.projectreactor.tools:blockhound` added to support reactive testing.

#### Pre configured settings:
- All Tests using [JUnit 5](https://junit.org/junit5/)
- min-max Heap size:  512m-2048m
- Run tests in parallel  (maxParallelForks = 3 | enable JUnit 6 parallel execution)
- Run tests by type only (Unit-, Integration- and E2E-test)
- Reactive Support: [Project Reactor](https://projectreactor.io/)
- 
---

### Unit-Test (`gradlew test`)
Unit Tests are as a gradle task definition all tests with Class names suffixed by "**Test**"
Please be aware that Unit-tests have to run very fast and often!  

---

### Integration-Test (`gradlew integrationTest`)
Tests which span multiple classes to verify e.g. use-case. No external dependencies should be loaded.
The integration-tests are recognized through the class suffix "**IntegrationTest**".

---

### End-to-End Test (`gradlew e2eTest`)
Testing with external dependencies e.g. database using  [Testcontainers](https://www.testcontainers.org).
This requires a installed and working [Docker](https://www.docker.com/) on the hosted machine.
Please be aware that End-to-End Tests are heavy in terms of hardware-resources and time.