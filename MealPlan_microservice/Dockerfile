FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar

# Expose app port and debug port
EXPOSE 9023
EXPOSE 5018

# Start JVM with remote debugging enabled
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5018", "-jar", "app.jar"]
