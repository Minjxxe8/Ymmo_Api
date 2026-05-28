FROM maven:3.9.16-eclipse-temurin-26-alpine

WORKDIR /app

COPY . .

RUN mvn clean package

FROM eclipse-temurin:26-jdk-noble

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]