FROM openjdk:8-jdk-alpine
ENV MYSQL_DB_PASSWORD password
VOLUME /tmp
COPY ./mycache/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.datasource.password=${MYSQL_DB_PASSWORD}"]