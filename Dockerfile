FROM maven:3.5-jdk-8 AS build

WORKDIR ./app

COPY pom.xml /app/pom.xml
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "dependency:go-offline", "-f", "/app/pom.xml"]

COPY ./src /app/src
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "package", "-DskipTests", "-f", "/app/pom.xml"]


FROM openjdk:8-jre-alpine3.9 AS runtime

ENV JAR_NAME ruchi-integration-1.0.jar

WORKDIR /app
COPY --from=build /app/target/$JAR_NAME /app/$JAR_NAME

COPY ./entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

RUN adduser -D dummy
USER dummy

CMD ["/app/entrypoint.sh"]
