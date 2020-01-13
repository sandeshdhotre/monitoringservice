FROM openjdk

WORKDIR /usr/app

COPY ./target/monitoring-service.jar /usr/app

EXPOSE 9010

ENTRYPOINT [ "java","-jar", "./monitoring-service.jar" ]
