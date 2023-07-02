# Partnerservice

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Technologies
- Quarkus 3.0
- Redis streams
- Debezium
- Postgresql
- MongoDB
- Java 11
- Docker
- Kubernetes
- Swagger UI
- Linode Server Deployment (only during development)

## Domain Driven Design
We created the PartnerRequest Entity according to the domain model that was defined during the modelling process.
_at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest_
## Event Sourcing
at.fhv.matchpoint.partnerservice.events.*
at.fhv.matchpoint.partnerservice.infrastructure.repository.EventRepository

at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest\
Apply and Process methods (67 - 172)
### Optimistic Locking
at.fhv.matchpoint.partnerservice.application.impl.PartnerRequestServiceImpl
Lines: 
- 79
- 105
- 131
- 179 - 183


### Message Ordering

### Message Tracking

## Interprocess Communication
Asynchronous Message Consumers
at.fhv.matchpoint.partnerservice.infrastructure.consumer.*
## CQRS
at.fhv.matchpoint.partnerservice.infrastructure.repository.MemberRepository
at.fhv.matchpoint.partnerservice.infrastructure.repository.PartnerRequestReadModel
at.fhv.matchpoint.partnerservice.infrastructure.consumer.MemberRepository
at.fhv.matchpoint.partnerservice.infrastructure.consumer.PartnerRequestReadModel
## Sagas
### Semantic Locking
at.fhv.matchpoint.partnerservice.events.request.RequestInitiatedEvent
at.fhv.matchpoint.partnerservice.events.request.AcceptPendingEvent

at.fhv.matchpoint.partnerservice.domain.model.PartnerRequest\
Lines:
- 120
- 130
- 140

## Role Based Authorization
at.fhv.matchpoint.partnerservice.rest.PartnerRequestResource\
@Authenticated Annotation in REST Controller Class
## API Gateway

### Authentication
### Fault tolerance
#### Circuit Breaker
#### Fallback


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/partnerservice-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- MongoDB client ([guide](https://quarkus.io/guides/mongodb)): Connect to MongoDB in either imperative or reactive style
- REST Client Reactive ([guide](https://quarkus.io/guides/rest-client-reactive)): Call REST services reactively
- MongoDB with Panache ([guide](https://quarkus.io/guides/mongodb-panache)): Simplify your persistence code for MongoDB via the active record or the repository pattern
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A JAX-RS implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Redis Client ([guide](https://quarkus.io/guides/redis)): Connect to Redis in either imperative or reactive style

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

### Debezium

Start MongoDB

```shell script
    docker compose up -d mongodb
```
Init MongoDB for replica

```shell script
    docker compose exec mongodb bash -c '/usr/local/bin/init.sh'
```
Start Debezium

```shell script
    docker compose up -d debezium'
```


