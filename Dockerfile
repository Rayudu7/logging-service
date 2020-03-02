FROM openjdk:8-jre-alpine

COPY build/distributions/loggin-service-1.0-SNAPSHOT.tar /opt/app/

WORKDIR /opt/app

RUN tar -xvf loggin-service-1.0-SNAPSHOT.tar

ENTRYPOINT "/opt/app/loggin-service-1.0-SNAPSHOT/bin/loggin-service"