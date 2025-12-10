FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar

  # Expose app port and debug port
EXPOSE 9009
EXPOSE 5002

  # Start JVM with remote debugging enabled
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5002", "-jar", "app.jar"]
