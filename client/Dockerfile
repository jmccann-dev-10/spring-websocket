FROM node AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN npm install \
    && npm run build

FROM nginx
COPY --from=build usr/src/app/build /usr/share/nginx/html