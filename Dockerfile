FROM maven:3.9.5-eclipse-temurin-8 as builder
COPY ./ /app
WORKDIR /app
RUN mvn clean install package

FROM eclipse-temurin:17.0.8.1_1-jdk-alpine
COPY --from=builder /app/target/elasticsearch_healthcheck-1.0-SNAPSHOT.one-jar.jar /elasticsearch_healthcheck.jar
CMD ["java", "-jar", "/elasticsearch_healthcheck.jar"]
