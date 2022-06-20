FROM openjdk:8
EXPOSE 8083
COPY "./target/outcome-product-service-0.0.1-SNAPSHOT.jar" "OutComeApplication"
ENTRYPOINT ["java", "-jar", "OutComeApplication"]