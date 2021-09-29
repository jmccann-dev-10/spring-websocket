FROM node:14-slim AS build
COPY . /usr/tmp
WORKDIR /usr/tmp
RUN npm install \
    && npm run build \
    && mv ./build /usr/src/app \
    && rm -r /usr/tmp

FROM nginx:latest
COPY --from=build usr/src/app /usr/share/nginx/html