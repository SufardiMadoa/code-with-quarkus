# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Run stage
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/quarkus-app/ /app/
CMD ["java", "-jar", "quarkus-run.jar"]
