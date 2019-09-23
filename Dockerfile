FROM maven:3.5-jdk-8 AS maven

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn package


FROM openjdk:8-jre-alpine3.9 AS runtime

ENV PORT 8080
RUN apk --no-cache add curl

RUN adduser -D dummy
USER dummy

WORKDIR /app

COPY --from=maven target/ruchi-integration-1.0.jar /app/app.jar
COPY entrypoint.sh /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh
CMD ["/app/entrypoint.sh"]

#HEALTHCHECK --interval=30s --timeout=3s \
#  CMD curl --silent --fail localhost:8080/health || exit 1
