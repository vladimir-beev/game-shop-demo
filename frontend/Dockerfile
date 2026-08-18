# ---- Build ----
FROM node:20 AS build
WORKDIR /app

# Accept build-time env var
ARG VITE_GATEWAY_URL
ENV VITE_GATEWAY_URL=$VITE_GATEWAY_URL

COPY package.json package-lock.json ./
RUN npm install

COPY . .
RUN npm run build

# ---- Runtime ----
FROM nginx:stable
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 3000
