FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8094
EXPOSE 8094
COPY target/query-logs-1.2-SNAPSHOT.jar query-logs.jar
ENTRYPOINT ["java","-jar","query-logs.jar"]