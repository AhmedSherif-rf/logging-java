FROM openjdk:17.0.1-jdk-slim

WORKDIR app
COPY ../target/ .
            
CMD java -jar service.jar --server.port=80