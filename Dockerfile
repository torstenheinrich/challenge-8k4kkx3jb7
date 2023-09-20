FROM openjdk:17-jdk-slim

ARG APPLICATION_USER=redcare-challenge
RUN useradd --user-group --create-home --no-log-init --shell /bin/bash $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/challenge-*-SNAPSHOT.jar /app/challenge.jar

WORKDIR /app

CMD ["java", "-jar", "challenge.jar"]
