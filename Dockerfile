FROM gradle:8-jdk23 AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM amazoncorretto:23-alpine AS runtime

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080
