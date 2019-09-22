FROM maven:3.5-jdk-8 as maven

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn package

#FROM openjdk:8-jre-alpine3.9
FROM jetty:9.4.18-jre8-alpine

WORKDIR /app
COPY --from=maven target/ruchi-integration-*.jar ./

CMD ["java", "-jar", "./ruchi-integration-1.0.jar"]