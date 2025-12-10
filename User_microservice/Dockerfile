FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9015 5010
ENTRYPOINT ["java", "-jar", "app.jar"]

