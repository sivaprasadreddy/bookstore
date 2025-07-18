# BookStore
A bookstore application built using SpringBoot as a monolith.

[![GitHub Actions](https://github.com/sivaprasadreddy/bookstore/actions/workflows/maven.yml/badge.svg)](https://github.com/sivaprasadreddy/bookstore/actions/workflows/maven.yml)

## Tech Stack:
* Spring Boot
* Spring Modulith
* Spring Data JPA
* Spring Security
* Thymeleaf
* Bootstrap CSS

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
