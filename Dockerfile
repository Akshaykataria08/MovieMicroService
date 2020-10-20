FROM openjdk:14-alpine
ADD /target/movieservice-0.0.1-SNAPSHOT.jar movieservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "movieservice-0.0.1-SNAPSHOT.jar"]