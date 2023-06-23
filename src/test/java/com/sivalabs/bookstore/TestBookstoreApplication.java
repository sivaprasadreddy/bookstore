package com.sivalabs.bookstore;

import com.sivalabs.bookstore.common.ContainersConfig;
import org.springframework.boot.SpringApplication;

public class TestBookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookstoreApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
