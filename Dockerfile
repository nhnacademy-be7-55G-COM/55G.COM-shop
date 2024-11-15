FROM eclipse-temurin:21

ENV SPRING_PROFILE="live"
ENV SERVER_PORT=8100

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

RUN mkdir /opt/app
COPY target/shop.jar /opt/app
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Dserver.port=${SERVER_PORT}", "-Duser.timezone=Asia/Seoul", "-Xms256m", "-Xmx256m", "-jar", "/opt/app/shop.jar"]
