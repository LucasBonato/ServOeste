FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean install

RUN ls -l /app/target

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/Serv-Oeste-0.0.1.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]