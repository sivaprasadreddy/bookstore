# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
BookStore is a Spring Boot shopping cart application built with Java 24, Spring Modulith, and Thymeleaf.

## Build Commands

### Maven Commands
- **Full build with tests**: `./mvnw clean verify`
- **Run application**: `./mvnw spring-boot:run`
- **Code formatting**: `./mvnw spotless:apply`
- **Docker image build**: `./mvnw clean compile spring-boot:build-image -DskipTests`

### Task Commands (using Taskfile)
- **Default (test)**: `task`
- **Run tests**: `task test`
- **Format code**: `task format`
- **Build Docker image**: `task build_image`
- **Start application**: `task start`
- **Stop application**: `task stop`

### Test Commands
- **Unit tests only**: `./mvnw test`
- **Integration tests only**: `./mvnw failsafe:integration-test`
- **Single test class**: `./mvnw test -Dtest=ClassName`
- **Test with coverage**: `./mvnw clean verify` (includes JaCoCo coverage with 70% minimum)

## Architecture

### Spring Modulith Structure
The application uses Spring Modulith to organize code into logical modules:
- **catalog**: Catalog management functionality (books, categories, etc.)
- **cart**: Cart features (add items, update quantities, etc)
- **common**: Cross-cutting concerns (entities, DTOs, utilities)
- **users**: Authentication and security
- **orders**: Order management functionality

Each module has a `package-info.java` file with `@ApplicationModule` annotations. The `common` module is marked as `Type.OPEN` to allow cross-module access.

### Technology Stack
- **Backend**: Spring Boot 3.5.3, Spring Data JPA, Spring Security, Spring Modulith
- **Database**: PostgreSQL with Flyway migrations
- **Frontend**: Thymeleaf, HTMX, Bootstrap CSS
- **Testing**: JUnit 5, Testcontainers, Spring Boot Test
- **Build**: Maven

## Development Setup

### Prerequisites
- JDK 24
- Docker and Docker Compose

### Local Development
1. Start with `local` profile: `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`
2. Access the application at http://localhost:8080

### Database
- Uses PostgreSQL in production
- Flyway migrations in `src/main/resources/db/migration/`

## Code Quality
- **Formatting**: Spotless with Palantir Java Format
- **Coverage**: JaCoCo with 70% minimum line coverage
- **Testing**: Comprehensive unit and integration tests using Testcontainers

## Configuration
- **Application Properties**: `ApplicationProperties.java` with `@ConfigurationProperties`
- **Profiles**: `local`, `test` configurations available
- **Environment Variables**: Prefix `APP_` for application properties
