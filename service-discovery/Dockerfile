FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8010
EXPOSE 8010
COPY target/discovery-service-1.2-SNAPSHOT.jar discovery-service.jar
ENTRYPOINT ["java","-jar","discovery-service.jar"]