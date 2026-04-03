FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/interview-service-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8083

ENV JAVA_OPTS="-Xms512m -Xmx1024m" \
    SPRING_PROFILES_ACTIVE="prod"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar app.jar"]
