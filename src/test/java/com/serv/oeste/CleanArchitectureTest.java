package com.serv.oeste;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.junit.AnalyzeClasses;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(
        packages = "com.serv.oeste",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class CleanArchitectureTest {
    private final static String presentationLayer = "Presentation";
    private final static String applicationLayer = "Application";
    private final static String domainLayer = "Domain";
    private final static String infrastructureLayer = "Infrastructure";

    @ArchTest
    static final ArchRule cleanArchitecture_LayerRules_ShouldBeFollowingLayers = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()

            .layer(presentationLayer).definedBy("..presentation..")
            .layer(applicationLayer).definedBy("..application..")
            .layer(domainLayer).definedBy("..domain..")
            .layer(infrastructureLayer).definedBy("..infrastructure..")

            .whereLayer(presentationLayer).mayNotBeAccessedByAnyLayer()
            .whereLayer(infrastructureLayer).mayNotBeAccessedByAnyLayer()
            .whereLayer(applicationLayer).mayOnlyBeAccessedByLayers(presentationLayer, infrastructureLayer)
            .whereLayer(domainLayer).mayNotAccessAnyLayer()
        ;

    @ArchTest
    static final ArchRule cleanArchitecture_DomainLayer_ShouldNotDependOnSpring = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideOutsideOfPackages("com.serv.oeste.domain..", "java..")
        ;

    @ArchTest
    static final ArchRule cleanArchitecture_ControllersOnPresentationLayer_ShouldNotAccessDomainDirectly = classes()
            .that().resideInAPackage("..presentation..")
            .should().onlyAccessClassesThat()
            .resideOutsideOfPackage("..domain.entities..")
        ;
}
