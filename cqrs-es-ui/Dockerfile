FROM gradle:8-jdk21

WORKDIR /app

COPY settings.gradle.kts build.gradle.kts gradle.properties /app/
COPY gradle/libs.versions.toml /app/gradle/libs.versions.toml
COPY composeApp /app/composeApp
COPY .fleet /app/.fleet

CMD ["gradle", "wasmJsBrowserRun", "-t", "--quiet"]

EXPOSE 8080
