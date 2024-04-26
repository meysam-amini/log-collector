FROM openjdk:17-alpine
#WORKDIR /opt
ENV PORT 8091
EXPOSE 8091
COPY target/auth-1.2-SNAPSHOT.jar auth.jar
ENTRYPOINT ["java","-jar","auth.jar"]