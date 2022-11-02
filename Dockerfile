FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/member-service-1.0.jar member-service.jar
ENTRYPOINT ["java", "-jar", "member-service.jar"]
