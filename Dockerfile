# Build stage
# use openjdk:8-jdk image to build the project. This image already has java installed.
# we can used the alpine version, but it gave me an error in bash
FROM openjdk:8-jdk AS build-env

# update the linux OS
RUN apt-get update

# Create app directory
WORKDIR /app

# copy in-built maven deployment tool to container, so that we don't have to install maven to the container
COPY mvnw .
COPY .mvn .mvn
# copy application code and the configuration
COPY src ./src
COPY pom.xml ./
COPY config/application.yml ./src/main/resources
# package the code into jar. Do not run any test while packaging
#RUN ./mvnw validate
RUN ./mvnw clean package -DskipTests

# Package stage
FROM openjdk:8-alpine3.9
WORKDIR /app
COPY --from=build-env /app/target/*.jar app.jar
ENTRYPOINT ["java","-Xmx2048m", "-jar","app.jar"]