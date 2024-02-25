

https://www.reactive-streams.org/
https://github.com/reactive-streams/reactive-streams-jvm

https://github.com/reactor/reactor (implements Reactive Streams specification)

https://gokhana.dev/spring-reactive-mongodb-implementation/
https://www.bezkoder.com/spring-boot-r2dbc-postgresql/

### What is reactive programming

Reactive programming is a programming paradigm. It’s actually been around for a while. Just like object-oriented programming, functional programming, or procedural programming, reactive programming is another programming paradigm.
The term, “reactive,” refers to programming models that are built around reacting to change network components reacting to I/O events, UI controllers reacting to mouse events, and others. In that sense, non-blocking is reactive, because, instead of being blocked, we are now in the mode of reacting to notifications as operations complete or data becomes available.

There is also another important mechanism that we on the Spring team associate with “reactive” and that is non-blocking back pressure. In synchronous, imperative code, blocking calls serve as a natural form of back pressure that forces the caller to wait. In non-blocking code, it becomes important to control the rate of events so that a fast producer does not overwhelm its destination.
For example a data repository (acting as Publisher) can produce data that an HTTP server (acting as Subscriber) can then write to the response. The main purpose of Reactive Streams is to let the subscriber control how quickly or how slowly the publisher produces data.

Paradigm is defined by Reactive Manifesto https://reactivemanifesto.org/

### Reactive Streams specification

Reactive Streams is a specification https://www.reactive-streams.org/

For Java programmers, Reactive Streams is an API. It is the product of a collaboration between engineers from Kaazing, Netflix, Pivotal, Red Hat, Twitter, Typesafe, and many others. Reactive Streams is much like JPA or JDBC. Both are API specifications.

The Reactive Streams API consists of just 4 high interfaces.

`Publisher` : A publisher is a provider of a potentially unbounded number of sequenced elements, publishing them according to the demand received from its Subscribers.
`Subscriber` : Will receive call to Subscriber.onSubscribe(Subscription) once after passing an instance of Subscriber to Publisher. subscribe(Subscriber).
`Subscription` : A Subscription represents a one-to-one lifecycle of a Subscriber subscribing to a Publisher.
`Processor` : A Processor represents a processing stage—which is both a Subscriber and a Publisher and obeys the contracts of both.
These concepts take different manifestations at different levels and areas. For eg. A java dev could think about how to program his at the application level, a database engineer could think how a database could react to reactive API calls (For example, Mongo DB has implemented a Reactive Streams driver), a network programmer could think how reactive calls could be made effective at the network level.

Some of the JVM-based frameworks that follow the Reactive Streams spec are:

1. Akka Streams framework
2. Ratpack
3. Vert.x
4. ReactiveX (RxJava 2.0, Reactor)
5. Java 1.9 Flow classes

**ReactiveX** is a combination of the best ideas from the Observer pattern, the Iterator pattern, and functional programming. It extends the observer pattern to support sequences of data and/or events and adds operators that allow you to compose sequences together declaratively while abstracting away concerns about things like low-level threading, synchronization, thread-safety, concurrent data structures, and non-blocking I/O.

**RxJava 2.0** and **Reactor** are based on ReactiveX project. And Spring WebFlux uses Reactor internally.


### Why was Spring WebFlux created?

- the need for a non-blocking web stack to handle concurrency with a small number of threads and scale with fewer hardware resources. Servlet non-blocking I/O leads away from the rest of the Servlet API, where contracts are synchronous (Filter, Servlet) or blocking (getParameter, getPart). This was the motivation for a new common API to serve as a foundation across any non-blocking runtime. That is important because of servers (such as Netty) that are well-established in the async, non-blocking space.

- the need for functional programming. Much as the addition of annotations in Java 5 created opportunities (such as annotated REST controllers or unit tests), the addition of lambda expressions in Java 8 created opportunities for functional APIs in Java. This is a boon for non-blocking applications and continuation-style APIs (as popularized by CompletableFuture and ReactiveX) that allow declarative composition of asynchronous logic. At the programming-model level, Java 8 enabled Spring WebFlux to offer functional web endpoints alongside annotated controllers.