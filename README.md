# account-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/account-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Run the application with Postgres on Docker as backend


``docker run --name postgresql -e POSTGRES_USER=quarkus_banking -e POSTGRES_PASSWORD=quarkus_banking -p 5432:5432 -v /c/temp:/var/lib/postgresql/data -d postgres``

The parameters pair the settings:

``
quarkus.datasource.username=quarkus_banking
quarkus.datasource.password=quarkus_banking
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost/quarkus_banking
``

-e POSTGRES_USER is the parameter that sets a unique username to the Postgres database.
-e POSTGRES_PASSWORD is the parameter that allows you to set the password of the Postgres database.
-p 5432:5432 is the parameter that establishes a connection between the Host Port and Docker Container Port. In this case, both ports are given as 5432, which indicates requests sent to the Host Ports will automatically redirect to the Docker Container Port. In addition, 5432 is also the same port where PostgreSQL will be accepting requests from the client.
-v is the parameter that synchronizes the Postgres data with the local folder. This ensures that Postgres data will be safely present within the Home Directory even if the Docker Container is terminated.
-d is the parameter that runs the Docker Container in the detached mode, i.e., in the background. If you accidentally close or terminate the Command Prompt, the Docker Container will still run in the background.
Postgres is the name of the Docker image that was previously downloaded to run the Docker Container.