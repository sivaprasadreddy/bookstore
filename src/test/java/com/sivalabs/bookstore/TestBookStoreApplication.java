package com.sivalabs.bookstore;

import com.sivalabs.bookstore.common.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestBookStoreApplication {

    public static void main(String[] args) {
        System.setProperty("spring.docker.compose.enabled", "false");
        SpringApplication.from(BookStoreApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
