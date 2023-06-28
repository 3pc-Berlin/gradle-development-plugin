# Docker Plugin
To enable easy docker support for java application we enabled the Plugin **[bmuschko - Gradle Docke Plugin](https://github.com/bmuschko/gradle-docker-plugin)**.
Additionally this plugin pre-configures:
- **Java base Image**: custom build by [3pc](https://www.3pc.de) and hostet on nexus.3pc.de
- **naming**: Gradle project name
- **tag**: Calculated semantic version

---
### Settings through environment variables

Docker repository configuration, set by CI or manually in local dev env.

| Env Variable               | Description                                   | Default Value  |
|----------------------------|-----------------------------------------------|----------------|
| `DOCKER_REPOSITORY_URL`    | Docker Repository URL                         | `nexus.3pc.de` |
| `DOCKER_USERNAME`          | Username used for the docker repository login | -              |
| `DOCKER_PASSWORD`          | Password used for the docker repository login | -              |
| `DOCKER_EXPOSE_PORT`       | Docker Expose Port for the Application        | `8080`         |
| `DOCKER_USER_EMAIL`        | E-mail (currently not used in code)           | -              |

System environment Variables to allow changing JAVA_OPTS parameters without changing application source code

- JAVA_OPTS_GLOBAL_DEFAULTS - set to override JAVA_OPTS_PLUGIN_DEFAULTS (hardcoded defaults, see `Docker.kt`). Standard JAVA_OPTS for all projects. E.g. provided by Kubernetes ConfigMap
- JAVA_OPTS_ADDITIONAL - add JAVA_OPTS specific to application (set by e.g. Helm Chart)

### .dockerignore
The plugin adds automatically a `.dockerignore` file to spring projects to increase docker build speed.
The source file is located under `/src/main/resources` inside the codebase of the plugin itself.
---

### Create, Build, Push and Remove
This plugin is set up pre-defined and configured tasks:
- `gradlew createDockerFile`: Creates a Dockerfile inside the `/build` using naming and versions given by gradle properties.
- `gradlew buildImage`: builds a Docker Image based on the `/build/DockerFile` using the local Docker Daemon.
- `gradlew pushImage`: push the docker image to nexus.3pc.de using given env Variables `DOCKER_USERNAME` and `DOCKER_PASSWORD`(REPO_3PC_USERNAME and REPO_3PC_PWD are still there for backward compatibility)
- `gradlew removeImage`: remove the docker image using gradle property project infos (name, version)


