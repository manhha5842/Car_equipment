# Sử dụng hình ảnh chính thức của OpenJDK làm base image
FROM openjdk:17-jdk-slim

# Đặt biến môi trường để chỉ định tên file JAR
ARG JAR_FILE=target/Car_equipment-0.0.1-SNAPSHOT.jar

# Sao chép file JAR vào trong container
COPY ${JAR_FILE} app.jar

# Đặt cổng mà ứng dụng sẽ chạy
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
