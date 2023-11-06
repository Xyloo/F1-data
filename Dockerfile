FROM gradle:8.4.0-jdk21-alpine AS BUILD
WORKDIR /usr/app/
ADD src ./src
COPY build.gradle settings.gradle .
RUN gradle build -x test

FROM openjdk:21
ENV JAR_NAME=F1-data-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME