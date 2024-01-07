FROM node:19-alpine
WORKDIR /app
COPY package*.json .
RUN npm i --verbose
RUN npm i @angular/cli@15.2.4
COPY .. .
EXPOSE 4200
CMD ["ng", "serve"]

