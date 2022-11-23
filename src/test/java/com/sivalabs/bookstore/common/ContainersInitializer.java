package com.sivalabs.bookstore.common;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class ContainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize (ConfigurableApplicationContext configurableApplicationContext){
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine").withExposedPorts(5432);
        KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.0.5-alpine")).withExposedPorts(6379);

        Startables.deepStart(postgres, redis, kafka).join();
        System.out.println("------------------overrideProperties---------------------------------");
        TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword(),
                "spring.data.redis.host=" + redis.getHost(),
                "spring.data.redis.port=" + redis.getFirstMappedPort(),
                "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}