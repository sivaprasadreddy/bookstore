package com.sivalabs.bookstore.common;

import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    private Integer port;

    @PostConstruct
    void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost:" + port;
    }
}
