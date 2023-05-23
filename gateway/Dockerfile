FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 10010
ADD ./target/gateway.jar ./app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]