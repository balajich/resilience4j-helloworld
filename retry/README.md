# Making RESTAPI fault tolerant using Retry Mechanism  
In  this tutorial we are going to learn how make spring boot based RESTAPI fault tolerant. Under certain circumstances we 
observed that subsystem works well when we retry the same request. To handle such scenarios we can use **resilience4j retry**
module.
- Using **Retry** automatically retry a failed call.
- This functionality can be achieved easily with annotation **@Retry** without writing explicit code. 

Overview
- User makes a call to greeting RESTAPI to get a greeting message.
- **greeting** method  returns valid response only when called three times.
- This behavior can be easily handled with annotation **Retry**
# Source Code 
``` git clone https://github.com/balajich/spring-cloud-session-7-microservices-centralized-configuration.git``` 
# Video
[![Spring Cloud Session-7 Centralized Configuration](https://img.youtube.com/vi/9hdTfz7NOqs/0.jpg)](https://www.youtube.com/watch?v=9hdTfz7NOqs)
- https://youtu.be/9hdTfz7NOqs
# Architecture
![architecture](architecture.png "architecture")
# Prerequisite
- JDK 1.8 or above
- Apache Maven 3.6.3 or above
- Vagrant, Virtualbox (To run RabbitMQ Server, Zapkin)
# Start RabbitMQ, Zipkin Servers and Build Microservices
We will be running RabbitMQ,Zipkin server inside a docker container. I am running docker container on CentOS7 virtual machine. 
I will be using vagrant to stop or start a virtual machine.

**Note:Zipkin is used for distributed tracing, Please follow my session-6 to understand it better**
- RabbitMQ & Zipkin Server
    - ``` cd  spring-cloud-session-7-microservices-centralized-configuration ```
    - Bring virtual machine up ``` vagrant up ```
    - ssh to virtual machine ```vagrant ssh ```
    - Switch to root user ``` sudo su - ```
    - Change folder where docker-compose files is available ```cd /vagrant```
    - Start RabbitMQ & Zipkin Server using docker-compose ``` docker-compose up -d ```
- Java
    - ``` cd  spring-cloud-session-7-microservices-centralized-configuration ```
    - ``` mvn clean install ```

# Running components
- Config Server: ``` java -jar .\config-server\target\config-server-0.0.1-SNAPSHOT.jar ```
- Registry: ``` java -jar .\registry\target\registry-0.0.1-SNAPSHOT.jar ```
- Employee API: ``` java -jar .\employee-api\target\employee-api-0.0.1-SNAPSHOT.jar ```
- Payroll API: ``` java -jar .\payroll-api\target\payroll-api-0.0.1-SNAPSHOT.jar ```
- Report API: ``` java -jar .\report-api\target\report-api-0.0.1-SNAPSHOT.jar ```
- Mail Client App: ``` java -jar .\mail-client\target\mail-client-0.0.1-SNAPSHOT.jar ```
- Gateway: ``` java -jar .\gateway\target\gateway-0.0.1-SNAPSHOT.jar ``` 

# Using curl to test environment
**Note I am running CURL on windows, if you have any issue. Please use postman client, its collection is available 
at  spring-cloud-session-7-microservices-centralized-configuration.postman_collection.json**
- Get employee report using report api ( direct): ``` curl -s -L  http://localhost:8080/report-api/100 ```
- Get Configurations of employee-api  with **default** profile``` curl -s -L http://localhost:8888/employee-api/default ```
- Get Configurations of report-api  with **default** profile``` curl -s -L http://localhost:8888/report-api/default ```
 
# Code
- All the microservices configurations are present in a folder **config-repo**
- In config-repo folder **application.yml** contains common configurations across microservices
- Save every microservice configuration with microservicename.yml. This will be default profile for the microservice
- For example employee-api.yml contains a configuration of employee-api microservice with default profile.
- Every microservice should have **boostrap.yml** that contains information to connect to config server.
- Every microservice should have **spring-cloud-config-server** and **spring-retry** as dependency

**pom.xml** for employee-api
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
</dependency>
```
**boostrap.yml** of employee-api, Where it points to configuration server
```yaml
 app.config-server: localhost
 
 spring:
   application.name: employee-api
   cloud.config:
     failFast: true
     retry:
       initialInterval: 3000
       multiplier: 1.3
       maxInterval: 10000
       maxAttempts: 20
     uri: http://localhost:8888
```
# References
- https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
- https://www.baeldung.com/resilience4j
- Spring Microservices in Action by John Carnell 
- Hands-On Microservices with Spring Boot and Spring Cloud: Build and deploy Java microservices 
using Spring Cloud, Istio, and Kubernetes -Magnus Larsson
# Next Tutorial
How to deploy microservices using docker
- https://github.com/balajich/spring-cloud-session-6-microservices-deployment-docker