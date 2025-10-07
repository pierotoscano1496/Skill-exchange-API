# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
EXPOSE 9081
ARG DEBUG=false
ENV JAVA_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
ENV JAVA_OPTS=""
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -fsS http://localhost:9081/api/actuator/health || exit 1
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT [ "sh", "-c", "exec java $JAVA_OPTS $([ \"$DEBUG\" = \"true\" ] && echo $JAVA_DEBUG_OPTS) -jar app.jar" ]
