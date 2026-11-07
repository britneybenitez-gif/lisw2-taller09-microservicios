FROM maven:3.9.9-eclipse-temurin-17 AS build
ARG MODULE
WORKDIR /build
COPY . .
RUN mvn -B -q -pl ${MODULE} -am -DskipTests package

FROM eclipse-temurin:17-jre
ARG MODULE
WORKDIR /app
RUN mkdir -p /app/data
COPY --from=build /build/${MODULE}/target/${MODULE}-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
