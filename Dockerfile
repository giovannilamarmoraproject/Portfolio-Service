FROM maven:3.9.7-eclipse-temurin-22 AS build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:22-jdk
EXPOSE 8080
WORKDIR /
COPY src/main/resources /app/resources
COPY --from=build /target/portfolio-service.jar portfolio-service.jar

ENTRYPOINT ["java","-jar","portfolio-service.jar"]
ENV TZ Europe/Rome