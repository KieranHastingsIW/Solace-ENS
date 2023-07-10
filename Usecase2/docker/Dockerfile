FROM maven:3.8.6-eclipse-temurin-17
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
CMD ["mvn", "clean","spring-boot:run"]