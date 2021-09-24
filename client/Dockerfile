FROM nginx
COPY public /usr/tmp/public
COPY src /usr/tmp/src
COPY package-lock.json /usr/tmp/
COPY package.json /usr/tmp/
RUN curl -fsSL https://deb.nodesource.com/setup_current.x | bash -
RUN apt-get install -y nodejs
WORKDIR /usr/tmp/
RUN npm install
RUN npm run build
RUN cp -r ./build/* /usr/share/nginx/html
RUN rm -r /usr/tmp/