FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /app
COPY . .

WORKDIR TelegramConnector

RUN gradle clean build -x test

FROM openjdk:21-jdk-slim AS runtime

RUN apt update && apt install -y tzdata curl
ENV TZ=Europe/Moscow
RUN apt update && apt install -y --no-install-recommends ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /app/TelegramConnector/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]