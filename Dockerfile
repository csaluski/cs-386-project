FROM openjdk:11-jre

ENV VERTICLE_FILE target/hello-verticle-1.0-SNAPSHOT.jar

#
ENV VERTICLE_NAME edu.nau.cs386.MainVerticle
ENV VERTICLE_FILE CS386-1.0.0-SNAPSHOT-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles/

EXPOSE 8888

# Copy your fat jar to the container
COPY build/libs/$VERTICLE_FILE /$VERTICLE_HOME
COPY src/main/psql/tables.psql /usr/psql/tables.psql

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $VERTICLE_FILE"]
