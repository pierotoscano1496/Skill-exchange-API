FROM amazoncorretto:17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY mvnw mvnw
RUN ./mvnw package -Dmaven.test.skip

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/skillexchangeapi-0.0.1-SNAPSHOT.jar app.jar
ENV PORT=9081
EXPOSE 9081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]