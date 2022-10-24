FROM openjdk:11
WORKDIR /home/azrael
COPY target/org.azrael.hash-0.0.1-SNAPSHOT.jar /home/azrael/azrael.jar
EXPOSE 8080
CMD java -Xms4096m -Xmx4096m -jar azrael.jar
