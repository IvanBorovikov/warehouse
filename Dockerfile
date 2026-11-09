FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/warehouse-backend-0.0.1-SNAPSHOT.jar /app/warehouse.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "warehouse.jar"]