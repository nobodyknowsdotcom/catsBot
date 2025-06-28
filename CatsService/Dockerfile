FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /app
COPY . .

WORKDIR CatsService

RUN gradle clean build -x test

FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app


COPY --from=builder /app/CatsService/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]