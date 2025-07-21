package com.sivalabs.bookstore.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:8.0.3")).withExposedPorts(6379);

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgres() {
        return postgres;
    }

    @Bean
    @ServiceConnection(name = "redis")
    GenericContainer<?> redisContainer() {
        return redis;
    }
}
