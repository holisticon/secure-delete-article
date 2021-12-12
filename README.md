[![CI](https://github.com/holisticon/secure-delete-article/actions/workflows/build.yml/badge.svg)](https://github.com/holisticon/secure-delete-article/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=de.holisticon.demos%3Asecure-delete-sample&metric=alert_status)](https://sonarcloud.io/dashboard?id=de.holisticon.demos%3Asecure-delete-sample)

- [Reference Architecture](#reference-architecture)
  - [Setup](#setup)
  - [Demo Data](#demo-data)
    - [Keycloak Users](#keycloak-users)
  - [Development](#development)

# Secure Do To List

A to do list that stores its data encrypted and GDPR-compliant using crypto shredding.
It was developed as a proof-of-concept and example for crypto shredding. 
It uses the [Keycloak GDPR module](https://github.com/toolisticon/keycloak-gdpr-module).

## Setup

Prerequisites:
* JDK 11+
* Docker
* NodeJS 12+

Start the application:
```bash
# Start MySQL database and Keycloak containers
docker-compose up -d

# build the application
./mvnw clean install

# start the backend
./mvnw spring-boot:run -f assembly

# start the frontend (in another terminal)
cd frontend
npm start
```

## Demo Data

To test the application, there are some preconfigured users in Keycloak:

* Master Realm (login to the Keycloak admin-console)
  * admin / admin
* App Realm (login to the demo app)
  * user1 / user1
  * user2 / user2


## Testing

### Performance Tests

For performance we're using [Gatling](https://gatling.io/docs/current/quickstart/):

```bash
$ ./mvnw gatling:test -f assembly
```
### Debugging

To debug the deployed gdpr-module in Keycloak:

```bash
$ docker compose up
```

then connect via Remote Debugging:

```
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9097
```
