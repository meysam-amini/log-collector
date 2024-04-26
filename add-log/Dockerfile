FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8090
EXPOSE 8090
COPY target/add-log-1.2-SNAPSHOT.jar add-log.jar
ENTRYPOINT ["java","-jar","add-log.jar"]