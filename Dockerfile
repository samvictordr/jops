# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the source code to the working directory
COPY src /app/src

# Compile the Java source code
RUN javac -d out src/com/sece/*.java

# Specify the command to run the application
CMD ["java", "-cp", "out", "com.sece.Main"]

