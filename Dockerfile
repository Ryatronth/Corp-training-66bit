FROM gradle:8-jdk23 AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM amazoncorretto:23-alpine AS runtime

ENV STORAGE_DIR=/app/storage

RUN mkdir -p $STORAGE_DIR

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8090
