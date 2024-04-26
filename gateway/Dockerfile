FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8082
EXPOSE 8082
COPY target/gateway-1.2-SNAPSHOT.jar gateway.jar
ENTRYPOINT ["java","-jar","gateway.jar"]