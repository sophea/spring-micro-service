===============================================================

#Running application

>> ./gradlew clean bootRun


#health check service API
go to browser : http://localhost:8080/api/healthcheck/v1

#swagger REST-API docs
http://localhost:8080/swagger-ui.html (dmi/secret)


-- Run standalone jar file
>> ./gradlew build
>> java -jar build/libs/app-1.0.1-SNAPSHOT.jar



The application can also be started manually by running

```
./gradlew clean check build

```
This will leave under the build/libs the application jar (together with sources and javadoc jars), which can be run manually (it's an executable jar file). In this case, jvm
options for debugging, memory size, and others can to be provided manually.

The application can also be run using docker containers. You need to have docker installed. To start the application run

```
./gradlew clean check dockerStart -PactiveProfile=DEV

```
This will package the application in a docker container and start it. Logs can be seen with ```docker ps``` and ```docker logs``` commands. To stop it, run
```
./gradlew dockerStop

```


 ==== release new version GIT

 ./gradlew clean release -Prelease.useAutomaticVersion=true

==