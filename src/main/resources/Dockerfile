FROM openjdk

WORKDIR /usr/app

COPY ./target/monitoring-service.jar .

EXPOSE 9010

ENTRYPOINT [ "java","-jar", "./monitoring-service.jar" ]
