FROM gradle:8-jdk21 AS build

RUN groupadd -g 999 appgroup && \
    useradd -r -u 999 -g appgroup appuser && \
    mkdir /home/appuser && \
    chown -R appuser /home/appuser && \
    chmod -R 700 /home/appuser

USER  appuser

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/

COPY src /app/src

RUN gradle assemble --no-daemon

FROM eclipse-temurin:21 AS final

RUN groupadd -g 999 appgroup && \
    useradd -r -u 999 -g appgroup appuser && \
    mkdir /home/appuser && \
    chown -R appuser /home/appuser && \
    chmod -R 700 /home/appuser

USER  appuser

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "app.jar", "--use-external-postgres"]