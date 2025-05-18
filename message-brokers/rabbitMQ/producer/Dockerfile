FROM maven:3.8.7-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# ✅ Allocate enough heap for large messages
ENV JAVA_OPTS="-Xmx2G"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
