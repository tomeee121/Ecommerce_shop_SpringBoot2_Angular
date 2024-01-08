FROM node:20.9.0 as builder
WORKDIR /app
COPY package.json ./
RUN npm cache clean --force
RUN npm cache verify
RUN npm i --force --package-lock-only
COPY . .
RUN npm run build --prod --verbose --package-lock-only

FROM nginx:1.21.6-alpine
COPY --from=builder /app/dist/shop-frontend /usr/share/nginx/html
EXPOSE 80
