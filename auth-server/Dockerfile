FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 10030
ADD ./target/auth-server.jar ./app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]