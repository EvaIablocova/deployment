FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar

# Expose app port and debug port
EXPOSE 9020
EXPOSE 5015

# Start JVM with remote debugging enabled
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5015", "-jar", "app.jar"]
