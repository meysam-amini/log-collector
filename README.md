# Log-Collector

### A scenario of microservices on Spring Cloud, for seamlessly gathering logs and sending them to third-parties. The CQRS pattern is used for distinguish between add-log apis and query-logs microservice. There is a syncer service, called sync-logs to add successfully sent logs to Elasticsearch, which gives us full text search on logs from query-logs service.

---

## Tech Stack
* JDK 17
* Spring-Boot 3.2.0
* Spring-Cloud 23.0.0
* Maven
* Surefire & Failsafe for unit and integration tests
* Postgres 14.2
* Kafka 7.4
* Keycloak 21.1.1
* Elasticsearch 8.3.3

---


## Architecture
The system contains following modules:

1. auth: This module is responsible for interacting with Keycloak for users registeration, authentication and authorization.
2. add-log: Gets logs on secured rest apis and then queues them. we can use a service for directly trying sending logs to third-parties, or there is another service in with queues logs for further processing. Feign client with decoder and retrier is used to interacting with third-party apis. 
3. query-logs: Exposes a service for full-text search on logs.
4. sync-logs: Contains kafka listeners for consuming successfully sent logs, to be synchronized with add-log service.
5. outbox-engine: An scheduler that trys to send unsent logs to third-parties in configurable times.
6. Spring Cloud elements: Spring CLoud Api Gateway, Config Server and Eureka as service registry.
7. external-services: sample service which simulates third-parties.

