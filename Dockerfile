FROM openjdk:18
WORKDIR /app
ARG JAR_FILE=target/skillexchangeapi-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9081
ENTRYPOINT ["java", "-jar", "app.jar"]