FROM adoptopenjdk:15-jre-hotspot
WORKDIR app
COPY target/maven-simple-0.2-SNAPSHOT.jar  logger.jar          
CMD java -jar "logger.jar" --server.port=80