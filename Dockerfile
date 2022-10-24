FROM openjdk:11

# Install maven
RUN apt-get update  
RUN apt-get install -y maven

WORKDIR /home/azrael
RUN git clone https://github.com/jmedina2099/Azrael.git

WORKDIR /home/azrael/Azrael
RUN mvn clean package

CMD java -Xms4096m -Xmx4096m -jar target/org.azrael.hash-0.0.1-SNAPSHOT.jar
