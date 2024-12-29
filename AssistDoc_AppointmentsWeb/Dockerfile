FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY target/Appointment-0.0.1-SNAPSHOT.jar /app/myapp.jar

EXPOSE 8090

CMD ["java", "-jar", "/app/myapp.jar"]
