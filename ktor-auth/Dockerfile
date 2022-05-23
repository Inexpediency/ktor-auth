FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*.jar /app/ktor-auth.jar
ENTRYPOINT ["java","-jar","/app/ktor-auth.jar"]
