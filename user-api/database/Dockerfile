FROM mariadb:latest

COPY *.sql /docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=top-secret-password
ENV MYSQL_DATABASE=dev10_users