# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -Dmaven.test.skip

# Stage 2: Run
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/skillexchangeapi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9081
ENTRYPOINT ["java", "-jar", "app.jar"]