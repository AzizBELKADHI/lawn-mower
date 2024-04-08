FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

COPY input.txt /app

COPY target/lawnmower-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "lawnmower-0.0.1-SNAPSHOT.jar"]