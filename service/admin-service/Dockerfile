FROM openjdk:17-jdk-slim
VOLUME /tmp
RUN apt-get update && apt-get install -y curl
COPY ./build/libs/*.jar admin-service.jar
ENTRYPOINT ["java", "-jar", "/admin-service.jar"]