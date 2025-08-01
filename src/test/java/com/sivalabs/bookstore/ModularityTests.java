package com.sivalabs.bookstore;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModularityTests {
    static ApplicationModules modules = ApplicationModules.of(BookStoreApplication.class);

    @BeforeAll
    static void beforeAll() {
        System.out.println("==============Modules================");
        System.out.println(modules);
        System.out.println("=====================================");
    }

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

    @Test
    void createModuleDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
