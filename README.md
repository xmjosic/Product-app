# Product Spring Boot application

Simple REST service that manages a list of products.

## Pre-requisites
- JDK 21+
- maven 3
- docker CLI (https://docs.docker.com/engine/install/)

Pull from git repository:
```shell
git pull -b main https://github.com/xmjosic/Product-app.git
```

Change directory to the root of this project.

Execute maven command:
```shell
mvn clean package
```

After successful build, see docker-compose.yml.
Docker compose file defines 2 services, one is PostgreSQL and other is this application.

Option 1: start both services in container

Run docker command:
```shell
docker-compose up -d
```

Database and application will start.

Option 2: Remove this application from docker-compose.yml file and run command:
```shell
docker-compose up -d
```
After successful start of a PostgreSQL container, run the application with command: 
```shell
java -jar target/product-1.0.0.jar
```
Or start application within your IDE.

### Actuator health
After running the database and application, be sure that application status is UP.

http://localhost:8081/actuator/health

Internet connection is required because of a HNB API integration.

### Documentation

Swagger documentation is available at:

#### Swagger UI

http://localhost:8081/swagger-ui.html

#### API Docs

http://localhost:8081/v3/api-docs





