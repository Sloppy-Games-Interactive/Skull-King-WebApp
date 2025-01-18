# Use an official Node.js image as a parent image
FROM node:22-alpine

# Set the working directory
WORKDIR /app

# Copy the package.json and yarn.lock files
COPY package.json yarn.lock ./

# Install dependencies
RUN yarn install #--frozen-lockfile

# Copy the rest of the application code
COPY . .

# Build the application
RUN yarn build-only

# Expose the port the application runs on
EXPOSE 5173

# Define the command to run the application
CMD ["node", "server.js"]