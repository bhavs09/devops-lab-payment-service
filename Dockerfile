FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY target/payment-service-1.0.0.jar app.jar

USER 10001

ENTRYPOINT ["java", "-jar", "app.jar"]