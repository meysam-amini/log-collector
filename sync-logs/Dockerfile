FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8095
EXPOSE 8095
COPY target/sync-logs-1.2-SNAPSHOT.jar sync-logs.jar
ENTRYPOINT ["java","-jar","sync-logs.jar"]