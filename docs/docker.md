## .dockerignore added
The plugin adds automaticaly a `.dockerignore` file to spring projects to increase docker build speed.
The source file is located under `/src/main/resources` inside the codebase of the plugin itself.

## Create, Build, Push and Remove
This plugin is set up pre-defined and configured tasks:
- `gradlew createDockerFile`: Creates a Dockerfile inside the `/build` using naming and versions given by gradle properties.
- `gradlew buildImage`: builds a Docker Image based on the `/build/DockerFile` using the local Docker Daemon.
- `gradlew pushImage`: push the docker image to nexus.3pc.de using given env Variables `DOCKER_USERNAME` and `DOCKER_PASSWORD`(REPO_3PC_USERNAME and REPO_3PC_PWD are still there for backward compatibility)
- `gradlew removeImage`: remove the docker image using gradle property project infos (name, version)