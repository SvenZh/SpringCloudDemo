FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 10050
ADD ./target/system-server.jar ./app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]