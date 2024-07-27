FROM openjdk:17.0.1-jdk-slim
WORKDIR app
COPY ../target/ .          
CMD java -jar "logging-0.8.0.jar" --server.port=80