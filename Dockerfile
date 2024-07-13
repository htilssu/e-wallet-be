FROM amazoncorretto:21-al2-jdk
LABEL authors="htilssu"
EXPOSE 80

# The application's jar file
ARG JAR_FILE=build/libs/*SNAPSHOT.jar

# Copy the application's jar to the container
COPY ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
