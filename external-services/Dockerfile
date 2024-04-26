FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8092
EXPOSE 8092
COPY target/external-services-1.2-SNAPSHOT.jar external-services.jar
ENTRYPOINT ["java","-jar","external-services.jar"]