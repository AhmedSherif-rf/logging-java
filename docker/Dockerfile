FROM openjdk:17.0.1-jdk-slim

WORKDIR app
COPY logging-0.8.0.jar .
            
CMD java -jar service.jar --server.port=80