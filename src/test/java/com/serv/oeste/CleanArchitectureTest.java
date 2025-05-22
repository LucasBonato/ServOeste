package com.serv.oeste;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class CleanArchitectureTest {
    JavaClasses importedClasses = new ClassFileImporter().importPackages("com.serv.oeste");

    private final String presentationLayer = "Presentation";
    private final String applicationLayer = "Application";
    private final String domainLayer = "Domain";
    private final String infrastructureLayer = "Infrastructure";

    @Test
    void cleanArchitecture_LayerRules_ShouldBeFollowingLayers() {
        ArchRule rule = layeredArchitecture()
                .consideringOnlyDependenciesInLayers()

                .layer(presentationLayer).definedBy("..presentation..")
                .layer(applicationLayer).definedBy("..application..")
                .layer(domainLayer).definedBy("..domain..")
                .layer(infrastructureLayer).definedBy("..infrastructure..")

                .whereLayer(presentationLayer).mayNotBeAccessedByAnyLayer()
                .whereLayer(infrastructureLayer).mayNotBeAccessedByAnyLayer()
                .whereLayer(applicationLayer).mayOnlyBeAccessedByLayers(presentationLayer, infrastructureLayer)
                .whereLayer(domainLayer).mayOnlyBeAccessedByLayers(applicationLayer, infrastructureLayer)
                .whereLayer(domainLayer).mayNotAccessAnyLayer()
            ;

        rule.check(importedClasses);
    }

    @Test
    void cleanArchitecture_DomainLayer_ShouldNotDependOnSpring() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework..");

        rule.check(importedClasses);
    }

    @Test
    void cleanArchitecture_ControllersOnPresentationLayer_ShouldNotAccessDomainDirectly() {
        ArchRule rule = classes()
                .that().resideInAPackage("..presentation..")
                .should().onlyAccessClassesThat()
                .resideOutsideOfPackage("..domain.entities..");

        rule.check(importedClasses);
    }
}
