FROM openjdk:17-ea-oracle
COPY target/*.jar language-service.jar
ENTRYPOINT [ "java", "-jar", "language-service.jar" ]
