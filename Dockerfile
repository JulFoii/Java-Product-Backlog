FROM maven:latest AS builder

WORKDIR /build
COPY . .

RUN mvn package -Pproduction

FROM openjdk:21-jdk-slim
LABEL org.opencontainers.image.source=https://github.com/JulFoii/Java-Product-Backlog

WORKDIR /app
COPY --from=builder /build/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]