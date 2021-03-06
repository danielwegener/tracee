> This document contains documentation for the `tracee-jaxws` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-jaxws

> This module can be used to add TracEE context propagation support to Java API for XML Web Services (JAX-WS) API for endpoints and webservice clients.

Please add the following dependencies to enable TracEE JAX-WS support. For example in maven-style projects add to the `pom.xml`:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.tracee.binding</groupId>
        <artifactId>tracee-jaxws</artifactId>
        <version>RELEASE</version>
    </dependency>
    ...
</dependencies>
```

## Using server side handlers
You can use the context logger by annotating your jax-ws webservice with the `@HandlerChain` annotation.

```java
@Stateless
@WebService(serviceName = "TraceeJaxWsTestService", portName = "TraceeJaxWsTestPort",
    targetNamespace = "https://github.com/tracee/tracee/examples/jaxws/service/wsdl")
@HandlerChain(file = "/traceeHandlerChain.xml")
public class TraceeJaxWsTestService implements TraceeJaxWsTestWS {
    ...
}
```
    
**Therefore you have to add the referenced `traceeHandlerChain.xml` file to your classpath (i.e. `/src/main/resources`).** The file must have the following content:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<javaee:handler-chains xmlns:javaee="http://java.sun.com/xml/ns/javaee">
    <javaee:handler-chain>
        <javaee:handler>
            <javaee:handler-class>io.tracee.binding.jaxws.TraceeServerHandler</javaee:handler-class>
        </javaee:handler>
    </javaee:handler-chain>
</javaee:handler-chains>
```    

## Using client side handlers
First you have to create the client stub classes for the webservice wsdl. 
Then you are able to bind the `TraceeClientHandler` by using the  handler resolver:

```java
final TraceeJaxWsTestService testWebservice = new TraceeJaxWsTestService(
    new URL("http://localhost:8080/yourTestService/webservices/YourTestService?wsdl"));
testWebservice.setHandlerResolver(new TraceeClientHandlerResolver());
final YourTestWS ws = testWebservice.getPort(YourTestWS.class);    
```

## Background

When the client sends a request to the server and he sends an response back, both kinds of message (request and response) have an outgoing and an incoming part. We can count four phases:

    Clients sends the request: RequestOut
    Server receives the request: RequestIn
    Server sends back the response: ResponseOut
    Client receives the response: ResponseIn

In JAX-WS we can  use HandlerChains to intercept requests and responses at client and server side.
Therefore TraEE offeres two kind of SOAPHandler: TraceeServerHandler and TraceeClientHandler.
