# Dockerfile for FxCalculator backend
FROM openjdk:21-jdk-slim AS build
LABEL authors="PO"

WORKDIR /app

COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src/ src/
COPY .env .

RUN chmod +x ./gradlew
RUN ./gradlew build

FROM openjdk:21-jdk-slim

VOLUME /tmp

COPY --from=build /app/.env /
#COPY --from=build /app/.env /app/libs/
COPY --from=build /app/build/ /app/

RUN ls -la /app/libs

EXPOSE 8090

ENTRYPOINT ["java","-jar","/app/libs/calculator-1.0.jar"]