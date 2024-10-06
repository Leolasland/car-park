FROM openjdk:21
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar