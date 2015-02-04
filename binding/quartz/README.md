> This document contains documentation for the tracee-quartz module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-quartz

This module contains a [Quartz](http://quartz-scheduler.org/) `JobListener` to generate a TracEE requestId before the job starts. This ID is send to other services if a entire binding is attached to the communication stack. 

* __TraceeJobListener__: Generates a request ID and clears the backend when the job has finished 
 
## Installation

Use this module with Quartz 2.1 or above. Add this module as dependency. For Maven:
```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.tracee.binding</groupId>
        <artifactId>tracee-quartz</artifactId>
        <version>RELEASE</version>
    </dependency>
    ...
</dependencies>
```

Add the `TraceeJobListener` like other job listeners:

### With a property file
```
org.quartz.jobListener.TraceeJobListener.class = io.tracee.binding.quartz.TraceeJobListener
```

### Dynamically with Java
```java
...
scheduler.getListenerManager().addJobListener(new TraceeJobListener());
...
```
