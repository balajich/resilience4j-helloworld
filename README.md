# Developing Fault tolerance services using Resilience4j  
In  this tutorial we are going to learn how make spring boot based RESTAPI fault tolerant.
- Using **Circuit Breaker** pattern helps us in preventing a cascade of failures when a remote service is down or slow.
After a number of failed attempts, we can consider that the service is unavailable/overloaded and eagerly reject all
subsequent requests to it. In this way, we can save system resources for calls which are likely to fail.  
- Using **Ratelimit** limit the number of call that a service can handle.
- Using **Bulkhead** to limit the number of concurrent calls to a  service.
- Using **Retry** automatically retry a failed call. 

**Note**: Please refer respective subfolders for documentation and example code.

