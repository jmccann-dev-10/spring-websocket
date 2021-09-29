FROM node AS build
# COPY . /usr/src/app
COPY . /usr/tmp
WORKDIR /usr/tmp
RUN npm install \
    && npm run build \
    && mv ./build /usr/src/app \
    && rm -r /usr/tmp

FROM nginx
COPY --from=build usr/src/app /usr/share/nginx/html