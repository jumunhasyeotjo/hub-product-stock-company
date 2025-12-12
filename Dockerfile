#FROM eclipse-temurin:21-jdk-jammy AS builder
#WORKDIR /app
#COPY . .
#RUN ./gradlew clean build -x test
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# JAR 파일 복사
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]