FROM maven:3.5-jdk-8 as maven

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn package

FROM openjdk:8-jre-alpine3.9
RUN apk --no-cache add curl

WORKDIR /app
COPY --from=maven target/ruchi-integration-1.0.jar ./
CMD ["java", "-jar", "./ruchi-integration-1.0.jar"]

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl --silent --fail localhost:8080/health || exit 1
