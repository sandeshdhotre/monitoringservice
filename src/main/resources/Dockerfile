FROM openjdk

WORKDIR /usr/app

COPY ../target/* /usr/app/

EXPOSE 9010

ENTRYPOINT [ "java","-jar", "./monitoring-service.jar" ]
