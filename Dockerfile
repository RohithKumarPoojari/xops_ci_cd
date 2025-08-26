FROM maven:3.8.5-openjdk-17 AS builder
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]

