FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package

FROM openjdk:11
EXPOSE 8080
WORKDIR /
COPY --from=build /target/portfolio-service.jar portfolio-service.jar

ENTRYPOINT ["java","-jar","portfolio-service.jar"]
ENV TZ Europe/Rome