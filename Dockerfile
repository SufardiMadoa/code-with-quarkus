# Gunakan image Java dengan Maven yang sudah siap pakai
FROM quay.io/quarkus/quarkus-micro-image:1.0

# Copy semua file ke image
COPY . /app

WORKDIR /app

# Build aplikasi
RUN ./mvnw package -DskipTests

# Jalankan aplikasi
CMD ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]
