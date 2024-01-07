FROM node:18.19-alpine as builder
WORKDIR /app
COPY package*.json .
RUN npm i --verbose
RUN npm i @angular/cli@13.3.3
COPY . .
RUN npm run build --prod

FROM nginx:1.21.6-alpine
COPY --from=builder /app/dist/shop-frontend /usr/share/nginx/html
EXPOSE 4200
