
# Dockerfile for FxCalculator Admin app (Angular)
FROM node:18-alpine as build
LABEL authors="PO"

WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

FROM nginx:alpine
COPY --from=build /app/dist/fxcalculator-admin /usr/share/nginx/html

ENTRYPOINT ["top", "-b"]
