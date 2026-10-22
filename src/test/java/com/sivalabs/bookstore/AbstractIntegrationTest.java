package com.sivalabs.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {
    @Autowired
    protected MockMvcTester mockMvcTester;
}
