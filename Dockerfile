# Stage 1: Build
FROM maven:3.8.5-openjdk-18 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -Dmaven.test.skip

# Stage 2: Run
FROM openjdk:18-slim
WORKDIR /app
COPY --from=build /app/target/skillexchangeapi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9081
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]

# Stage 3: MongoDB with initialization (Prod)
FROM mongodb/mongodb-community-server:5.0-ubuntu2004
COPY docker/mongo/init/001-create-app-user.js /docker-entrypoint-initdb.d/001-create-app-user.js
