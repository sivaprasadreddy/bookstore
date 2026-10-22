# BookStore
A bookstore application built using SpringBoot as a monolith.

[![GitHub Actions](https://github.com/sivaprasadreddy/bookstore/actions/workflows/maven.yml/badge.svg)](https://github.com/sivaprasadreddy/bookstore/actions/workflows/maven.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sivaprasadreddy_bookstore&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sivaprasadreddy_bookstore)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sivaprasadreddy_bookstore&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sivaprasadreddy_bookstore)

## Tech Stack:
* Java
* Spring Boot
* Spring Modulith
* Spring Data JPA
* Spring Security
* Thymeleaf
* Bootstrap CSS

## Development Setup

### Prerequisites
- JDK 25 or later
- Docker and Docker Compose

### Local Development
1. Start with `local` profile: `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`
2. Access the application at http://localhost:8080

## How to run?

```shell
$ ./mvnw verify
$ ./mvnw spring-boot:run
```

# Running the application on Kubernetes

* Start kind cluster

```shell
cd deployment/kind
./kind-cluster.sh create
```

* Deploy/Undeploy application

```shell
cd deployment
kubectl apply -f k8s/
kubectl delete -f k8s/
```
