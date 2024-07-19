# The Slightly More Complex Thingy (TSMCT)

TSMCT is a comprehensive project designed to showcase the implementation of CQRS (Command Query Responsibility Segregation) and ES (Event Sourcing) patterns, along with a dedicated UI component to interact with the system. This project is split into two main parts: `cqrs-es` for the backend logic and `cqrs-es-ui` for the frontend interface.

The project is built using Kotlin for the backend and the frontend using Kotlin Multiplatform Compose. The backend part is implemented using the CQRS and ES patterns with the simplest possible thing from Greg Young as reference. The project is slightly more complex in a way that it uses a PostgreSQL database and the Spring ApplicationEventPublisher to publish events.

## Project Structure

- **cqrs-es**: This is the backend part of the project, implementing CQRS and ES patterns. It handles commands, queries, and events to ensure that the system's write and read operations are decoupled and optimized for scalability and maintainability. As underlying technologies, it uses Spring Boot, Kotlin, and PostgreSQL. The backend can be run with an [embedded PostgreSQL database](https://github.com/zonkyio/embedded-postgres) or use an external database. To use an external database the argument `--use-external-postgres` has to be set and the respective connection properties have to be configured. This is just done for localhost in the moment.
  
- **cqrs-es-ui**: The frontend part of the project, providing a user interface to interact with the backend. It's designed to demonstrate how commands and queries can be issued from a user interface to a system built on the CQRS and ES principles. The frontend is built using Kotlin Multiplatform Compose.

## Prerequisites

To run both `cqrs-es` and `cqrs-es-ui`, you need to have the following installed on your system:

- Java 11 or newer (I would recommend using Java 21, which I used for development)
- Kotlin
- Gradle
- Yarn (for `cqrs-es-ui`)
- Docker and Docker Compose

Ensure that your environment is set up with these prerequisites before proceeding with the setup and testing of the projects.

## Testing the cqrs-es Version

To test the `cqrs-es` part of the project, follow these steps:

1. Navigate to the `cqrs-es` directory.
2. Run `./gradlew test` to execute the unit tests. This will test the core functionalities of the CQRS and ES implementations. This is also using the embedded PostgreSQL database.


## Docker Compose Setup

This project includes a `docker-compose.yml` file to simplify the setup of external dependencies such as databases. To use it, follow these steps:

1. Ensure Docker and Docker Compose are installed on your system.
2. From the root of the project, run `docker-compose up -d` to start the required services in detached mode.

This will start all three components: the PostgreSQL database, the `cqrs-es` backend, and the `cqrs-es-ui` frontend. You can then access the frontend at `http://localhost:8081` and the backend at `http://localhost:8080`. The database will be available on `localhost:5432`.

The Dockerfile for the frontend starts the frontend with `gradle wasmJsBrowserRun -t --quiet` which seems to start a development server. Starting it in this way takes a few minutes. You can see when the frontend is ready when you see the following message in the logs of the Docker container:

```
Waiting for changes to input files...
```