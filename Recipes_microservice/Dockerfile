FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar

# Expose app port and debug port
EXPOSE 9022
EXPOSE 5017

# Start JVM with remote debugging enabled
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5017", "-jar", "app.jar"]
