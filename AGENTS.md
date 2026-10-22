# Guidelines for AI Coding Agents

This file provides guidance to AI coding agents when working with code in this repository.

## Project Overview
BookStore is a Spring Boot shopping cart application built with Java, Spring Modulith, and Thymeleaf.

## Build Commands

### Maven Commands
- **Full build with tests**: `./mvnw clean verify`
- **Run application**: `./mvnw spring-boot:run`
- **Code formatting**: `./mvnw spotless:apply`
- **Docker image build**: `./mvnw clean compile spring-boot:build-image -DskipTests`

### Test Commands
- **Unit tests only**: `./mvnw test`
- **Integration tests only**: `./mvnw failsafe:integration-test`
- **Single test class**: `./mvnw test -Dtest=ClassName`
- **Test with coverage**: `./mvnw clean verify` (includes JaCoCo coverage with 70% minimum)

## Architecture

### Spring Modulith Structure
The application uses Spring Modulith to organize code into logical modules:
- **common**: Cross-cutting concerns (entities, DTOs, utilities)
- **catalog**: Catalog management functionality (books, categories, etc.)
- **cart**: Cart features (add items, update quantities, etc)
- **users**: Authentication and security
- **orders**: Order management functionality

### Technology Stack
- **Backend**: Java, Spring Boot, Spring Data JPA, Spring Security, Spring Modulith
- **Database**: PostgreSQL with Flyway migrations
- **Frontend**: Thymeleaf, HTMX, Bootstrap CSS
- **Testing**: JUnit, Testcontainers, Spring Boot Test
- **Build**: Maven
