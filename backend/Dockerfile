# Use an official Scala image as a parent image
FROM sbtscala/scala-sbt:eclipse-temurin-jammy-22_36_1.10.0_3.4.2

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Install dependencies and build the project
RUN sbt clean compile

# Expose the port the application runs on
EXPOSE 9000

# Define the command to run the application
CMD ["sbt", "run"]