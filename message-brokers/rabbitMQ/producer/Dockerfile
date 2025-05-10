# 🏗️ Use Maven to build the app
FROM maven:3.8.7-amazoncorretto-17 AS build

WORKDIR /app

# 👇 Copy pom.xml & download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# 👇 Copy source code
COPY src ./src

# 👇 Package the application
RUN mvn clean package -DskipTests

# 🏃 Now create the final image
FROM amazoncorretto:17-alpine

WORKDIR /app

# 👇 Copy the built jar from the builder stage
COPY --from=build /app/target/*.jar app.jar

# 🔥 Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
