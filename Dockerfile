FROM eclipse-temurin:21

ENV SPRING_PROFILE="live"
ENV SERVER_PORT=8100

RUN mkdir /opt/app
COPY target/shop.jar /opt/app
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Dserver.port=${SERVER_PORT}","-jar", "/opt/app/shop.jar"]
