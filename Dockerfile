FROM eclipse-temurin:24-jdk AS builder

WORKDIR /app
COPY . .

RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
