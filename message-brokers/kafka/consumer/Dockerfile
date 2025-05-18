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

# Set heap size to handle large messages (up to 100MB)
ENV JAVA_OPTS="-Xmx2G"

EXPOSE 8084
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
