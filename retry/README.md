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
- [https://github.com/balajich/resilience4j-helloworld/tree/master/retry](https://github.com/balajich/resilience4j-helloworld/tree/master/retry) 
# Video
[![Spring Cloud Session-7 Centralized Configuration](https://img.youtube.com/vi/9hdTfz7NOqs/0.jpg)](https://www.youtube.com/watch?v=9hdTfz7NOqs)
- https://youtu.be/9hdTfz7NOqs
# Architecture
![architecture](architecture.png "architecture")
# Prerequisite
- JDK 1.8 or above
- Apache Maven 3.6.3 or above
# Build
- ``` cd  resilience4j-helloworld ```
- ``` mvn clean install ```

# Running RESTAPI
- REST API Server: ``` java -jar .\retry\target\retry-0.0.1-SNAPSHOT.jar ```

# Using curl to test environment
- Call Greeting API: ``` curl -s -L  http://localhost:8080/greeting ```
# Using JMeter
- JMeter Script is provided to generate two successive calls.
-  Import **resilience4j-helloworld.jmx** and run retry thread group.
- ![jmeter](jmeter.png "jmeter")
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