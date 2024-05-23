package com.sivalabs.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenAPI3Configuration {

    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookStore APIs")
                        .description("BookStore REST APIs")
                        .version("v1.0.0")
                        .contact(new Contact().name("SivaLabs").email("sivalabs@sivalabs.in")));
    }
}
