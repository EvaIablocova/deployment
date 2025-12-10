FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9014 5009
ENTRYPOINT ["java", "-jar", "app.jar"]
