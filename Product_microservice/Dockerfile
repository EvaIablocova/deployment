FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9019 5014
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5014", "-jar", "app.jar"]
