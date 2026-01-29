# Project Overview and Developer Guide

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

Each microservice uses Maven wrapper. Run from the repository root:

```bash
# Build a single service
./Task_microservice/mvnw -f Task_microservice/pom.xml clean package

# Build all services (skip tests for faster builds)
./User_microservice/mvnw -f User_microservice/pom.xml clean package -DskipTests
./Task_microservice/mvnw -f Task_microservice/pom.xml clean package -DskipTests
./List_microservice/mvnw -f List_microservice/pom.xml clean package -DskipTests
./Reporting_microservice/mvnw -f Reporting_microservice/pom.xml clean package -DskipTests
./Feedback_microservice/mvnw -f Feedback_microservice/pom.xml clean package -DskipTests
./Competition_microservice/mvnw -f Competition_microservice/pom.xml clean package -DskipTests
./Database_microservice/mvnw -f Database_microservice/pom.xml clean package -DskipTests
./API_Gateway/mvnw -f API_Gateway/pom.xml clean package -DskipTests

# Run tests for a single service
mvn -f Task_microservice/pom.xml test

# Run a specific test class
mvn -f Task_microservice/pom.xml test -Dtest=TaskControllerTest

# Run a specific test method
mvn -f Task_microservice/pom.xml test -Dtest=TaskControllerTest#testMethodName
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Docker Commands

```bash
# Create network (required first time)
docker network create my_custom_microservices_network

# Build and start all services
docker-compose up --build

# Stop services
docker-compose down
```

## Architecture

This is a Spring Boot 3 microservices project (Java 21) called **HouseMate**.

### Service Communication Pattern
```
Client → API_Gateway (8099)
              ↓ RestTemplate
         Microservices (9010-9017)
              ↓ RestTemplate
         Database_microservice (9009)
              ↓ JPA/Hibernate
         MySQL (3336)
```

### Services and Ports

| Service | App Port | Debug Port | Docker Container Name |
|---------|----------|------------|----------------------|
| API_Gateway | 8099 | 5006 | apigateway |
| Database_microservice | 9009 | 5002 | dbmicroservice |
| Task_microservice | 9010 | 5007 | taskmicroservice |
| Competition_microservice | 9011 | 5008 | competitionmicroservice |
| List_microservice | 9014 | 5009 | listmicroservice |
| User_microservice | 9015 | 5010 | usermicroservice |
| Feedback_microservice | 9016 | 5011 | feedbackmicroservice |
| Reporting_microservice | 9017 | 5012 | reportingmicroservice |
| MySQL | 3336 | - | my_mysql |

### Inter-Service Communication

Services communicate via REST using RestTemplate:
- API_Gateway routes to services using Docker container names (e.g., `http://taskmicroservice:9010/api/tasks`)
- Microservices call Database_microservice for persistence (e.g., `http://dbmicroservice:9009/db/tasks`)

### Standard Service Structure

Each microservice follows this pattern:
```
*_microservice/
├── src/main/java/com/example/*_microservice/
│   ├── *Application.java          # Main entry point
│   ├── config/OpenApiConfig.java  # Swagger config
│   ├── controller/                # REST endpoints
│   ├── service/                   # Business logic
│   ├── repository/                # Data access / external calls
│   └── DTOs/                      # Data transfer objects
├── src/main/resources/application.properties
├── src/test/java/                 # Unit tests
├── Dockerfile
└── pom.xml
```

### Key Technical Details

- **Database**: Only Database_microservice connects to MySQL; all others use RestTemplate to call it
- **DDL**: Hibernate manages schema with `ddl-auto=update`
- **API Docs**: Swagger UI available at `http://localhost:{port}/swagger-ui/index.html`
- **CI/CD**: GitHub Actions runs tests on push/PR to main (see `.github/workflows/tests.yml`)
- **Tests**: Unit tests exist for all services except Database_microservice and API_Gateway
