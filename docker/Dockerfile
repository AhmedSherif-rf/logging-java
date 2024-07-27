FROM adoptopenjdk:15-jre-hotspot
WORKDIR app
COPY target/jberet-simple.jar  logger.jar          
CMD java -jar logger.jar simple.xml jobParam1=x jobParam2=y