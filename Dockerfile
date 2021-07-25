FROM gradle:7.1.1-jdk11

ENV APP_HOME=/usr/app
COPY ./*.gradle.kts ./gradlew* ./system.properties ./src ./gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR $APP_HOME

USER root
RUN chown -R gradle /home/gradle/src

RUN gradle --no-daemon shadowJar

ENV VERTICLE_NAME edu.nau.cs386.MainVerticle
ENV VERTICLE_FILE CS386-1.0.0-SNAPSHOT-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles/

EXPOSE 8888

# Copy your fat jar to the container
COPY build/libs/$VERTICLE_FILE $VERTICLE_HOME
COPY $APP_HOME/src/main/psql/tables.psql /usr/psql/tables.psql

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
