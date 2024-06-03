package com.sivalabs.bookstore;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ArchUnitTests {
    private static final String ROOT_PACKAGE = "com.sivalabs.bookstore";

    JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT_PACKAGE);

    @Test
    void shouldNotUseFieldInjection() {
        noFields().should().beAnnotatedWith(Autowired.class).check(importedClasses);
    }

    @Test
    void shouldNotUseJunit4Classes() {
        JavaClasses classes = new ClassFileImporter().importPackages(ROOT_PACKAGE);

        noClasses()
                .should()
                .accessClassesThat()
                .resideInAnyPackage("org.junit")
                .because("Tests should use Junit5 instead of Junit4")
                .check(classes);

        noMethods()
                .should()
                .beAnnotatedWith("org.junit.Test")
                .orShould()
                .beAnnotatedWith("org.junit.Ignore")
                .because("Tests should use Junit5 instead of Junit4")
                .check(classes);
    }
}
