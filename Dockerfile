FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY checkstyle.xml checkstyle.xml

RUN ./mvnw clean package -DskipTests -Dcheckstyle.skip

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS=""
#ENV SPRING_PROFILES_ACTIVE="prod"

EXPOSE 6001

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
