# Stage 1: Build the application
FROM maven:3.8.7-amazoncorretto-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Increase heap size for large Kafka messages (adjust as needed)
ENV JAVA_OPTS="-Xms2G -Xmx2G"

EXPOSE 8084

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
