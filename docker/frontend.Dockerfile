FROM node:18-alpine

COPY ./frontend /app

WORKDIR /app

RUN yarn install

RUN yarn build-only

# Expose the port the app runs on
EXPOSE 3000

# Command to start the server
CMD ["node", "server.js"]