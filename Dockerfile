# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the source code and MySQL connector library to the container
COPY src /app/src
COPY lib /app/lib

# Create a bin directory for compiled classes
RUN mkdir /app/bin

# Compile the Java source code with the MySQL connector library
RUN javac -cp ".:/app/lib/mysql-connector-j-9.1.0.jar" -d bin src/com/sece/*.java

# Set the classpath to include the MySQL connector and compiled classes
CMD ["java", "-cp", ".:/app/lib/mysql-connector-j-9.1.0.jar:/app/bin", "com.sece.Main"]
