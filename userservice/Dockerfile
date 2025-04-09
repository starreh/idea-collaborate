FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM registry.access.redhat.com/ubi8/openjdk-17

WORKDIR /app

COPY --from=build /app/target/userservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-XX:+IgnoreUnrecognizedVMOptions", "-XX:+UseContainerSupport", "-Dmicrometer.metrics.export.defaults.enabled=false", "-jar", "app.jar"]