FROM ubuntu:latest
LABEL authors="usuario"

WORKDIR /app
COPY target/Programa-Service-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-jar","Programa-Service-0.0.1-SNAPSHOT.jar"]