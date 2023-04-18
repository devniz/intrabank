package com.mastercard.intrabank;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "uk.com.mastercard", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureTests {

    /**
     * This codebase adopts Domain Driven Design principles and the onion architecture.
     * <p>
     * This means there are some rules governing the packages that code should be added to,
     * and the lines of communication between the code, as illustrated below.
     * <p>
     * These rules are encoded in the archRule architecture test, which will need
     * updating if new adapters are added to the project.
     * <p>
     * Domain layer: business logic
     * Application services: orchestration of domain layer and infra
     * Controllers / Consumers: ingress points into the application (e.g. REST APIs, Kafka consumers)
     * Infra: Supporting code for integration with downstream services, persistence etc.
     * <p>
     * ┌──────────────────────────────────────────────────────────────────────────┐
     * │       Controllers/Consumers (application.{controller, consumer} )        │
     * └─────────────────────────────────────┬────────────────────────────────────┘
     * │
     * ┌─────────────────────────────────────▼────────────────────────────────────┐
     * │                Application Services (application.service)                │
     * └─────────────────────────────────────┬────────────────────────────────────┘
     * │                                   │
     * │                                   │
     * │    ┌──────────────────┬───────────▼────────────────┬────────────────┐
     * │    │                  │  (domain.{model,service})  │                │
     * │    │   ┌──────────────┴─────────────┬┬─────────────┴──────────────┐ │
     * │    │   │        Domain Model        ││      Domain Services       │ │
     * │    │   └────────────────────────────┘└────────────────────────────┘ │
     * │    ├─────────────────────┬──────────────────────────────────────────┘
     * │    │    Domain Layer     │
     * │    └─────────────────────┘
     * │
     * ┌─▼────────────────────────────────────────────────────────────────────────┐
     * │                       Infrastructure Layer (infra)                       │
     * └───────────────┬─────────────────────┬─────────────────────┬──────────────┘
     * │      Adapter 1      │      Adapter 2      │
     * └─────────────────────┴─────────────────────┘
     */

    @ArchTest
    static final ArchRule archRule = onionArchitecture()
            .applicationServices("uk.co.mastercard.application..")
            .domainModels("uk.co.mastercard.domain..model..")
            .domainServices("uk.co.mastercard.domain..service..")
            .adapter("config", "uk.co.mastercard.infra.inmemory..")
            .withOptionalLayers(true);
}
