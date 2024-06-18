FROM eclipse-temurin:21
COPY target/product-1.0.0.jar /app/product-1.0.0.jar
ENTRYPOINT ["java", "-jar", "/app/product-1.0.0.jar"]