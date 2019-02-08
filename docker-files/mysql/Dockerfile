FROM mysql
ENV MYSQL_DATABASE recipe
ENV MYSQL_USER recipe
COPY ./docker-files/mysql/schema-generation-mysql.sql /docker-entrypoint-initdb.d/