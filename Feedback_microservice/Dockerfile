FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9016 5011
ENTRYPOINT ["java", "-jar", "app.jar"]
