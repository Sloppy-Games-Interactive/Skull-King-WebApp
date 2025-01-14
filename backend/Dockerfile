# Use an official Scala image as a parent image
FROM hseeberger/scala-sbt:11.0.11_1.5.5_2.13.6

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