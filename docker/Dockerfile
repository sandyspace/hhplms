FROM primetoninc/jdk:1.8

LABEL maintainer="shancheng1981 <19009790@qq.com>"

ENV ACTIVE_PROFILE=test

ENV TIME_ZONE=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

ADD hhplms-1.0.0.jar /opt/hhplms-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["/bin/bash", "-c", "java -server -jar -Dspring.profiles.active=$ACTIVE_PROFILE /opt/hhplms-1.0.0.jar"]