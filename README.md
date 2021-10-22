[![CI](https://github.com/holisticon/secure-delete-article/actions/workflows/build.yml/badge.svg)](https://github.com/holisticon/secure-delete-article/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=de.holisticon.demos%3Asecure-delete-sample&metric=alert_status)](https://sonarcloud.io/dashboard?id=de.holisticon.demos%3Asecure-delete-sample)

- [Reference Architecture](#reference-architecture)
  - [Setup](#setup)
  - [Demo Data](#demo-data)
    - [Keycloak Users](#keycloak-users)
  - [Development](#development)

# Reference Architecture

> Reference stack demonstrating the build and application composition with Keycloak, SpringBoot, Kotlin, Swagger, Angular and TypeScript.

## Setup

Prerequisites:
* JDK 11+
* Docker
* NodeJS 12+

Start the docker stack:

```
docker-compose up -d
```

## Demo Data

### Keycloak Users

* Master Realm
  * admin / admin
* App Realm
  * user1 / user1
  * user2 / user2

## Development

```
./mvn clean install
```

**Backend**
```
(cd assembly/ && ../mvnw spring-boot:run  -Dsprprofiles.active=development)
```
**Frontend**
```
(cd frontend/ && npm start)
```

## Testing

### Performance Tests

For performance we're using (Gatling)[https://gatling.io/docs/current/quickstart/]:

```
(cd assembly/ && ../mvnw gatling:test)
```
