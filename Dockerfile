FROM openjdk:18
EXPOSE 9081
ADD target/skillexchangeapi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]