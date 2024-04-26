FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8093
EXPOSE 8093
COPY target/outbox-engine-1.2-SNAPSHOT.jar outbox-engine.jar
ENTRYPOINT ["java","-jar","outbox-engine.jar"]