FROM maven:3.9.5-eclipse-temurin-21-alpine AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

COPY --from=builder --chown=appuser:appgroup /build/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]