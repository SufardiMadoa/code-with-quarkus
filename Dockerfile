# Tahap build: gunakan Maven dengan JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Tahap run: gunakan JDK 21
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*-runner.jar app.jar

# JAVA_HOME biasanya sudah otomatis diatur di image ini, tapi bisa ditambahkan secara eksplisit jika perlu
ENV JAVA_HOME=/opt/java/openjdk

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
