FROM openjdk:18
ARG JAR_FILE=out/artifacts/skillexchangeapi_jar/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9081
ENTRYPOINT ["java", "-jar", "/app.jar"]