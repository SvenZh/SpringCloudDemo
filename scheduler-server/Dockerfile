FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 10040
ADD ./target/scheduler-server.jar ./app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]