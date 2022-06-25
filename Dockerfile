FROM openjdk:8
VOLUME /tmp
EXPOSE 8083
COPY ./target/outcome-product-service-0.0.1-SNAPSHOT.jar outcome-product-service.jar
ENTRYPOINT ["java", "-jar", "outcome-product-service.jar"]