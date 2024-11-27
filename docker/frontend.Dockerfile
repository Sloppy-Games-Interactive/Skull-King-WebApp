FROM node:18-alpine

ARG CACHEBUST=1

RUN echo $CACHEBUST

COPY ./frontend /app

WORKDIR /app

RUN yarn install --frozen-lockfile

RUN yarn build-only

EXPOSE 3000

CMD ["node", "server.js"]