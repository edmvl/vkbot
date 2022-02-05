FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} vkbot-1.0.0.jar
ENTRYPOINT ["java","-jar","/vkbot-1.0.0.jar"]