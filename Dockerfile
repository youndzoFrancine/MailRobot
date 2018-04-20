
FROM openjdk:8-jdk

EXPOSE 8282

VOLUME /data

ADD   SmtpClient-build-1.0-SNAPSHOT.jar  /app/service.jar


CMD ["java", "-jar", "/app/service.jar"]