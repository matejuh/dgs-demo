import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    id("com.netflix.dgs.codegen") version "6.0.2"
    id("nu.studer.jooq") version "8.2.1"
    id("com.ncorti.ktfmt.gradle") version "0.13.0"
    id("com.adarshr.test-logger") version "3.2.0"
}

group = "com.productboard"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform:7.5.1")
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.1.3")
        mavenBom("org.zalando:logbook-bom:3.4.0")
    }
    dependencies {
        dependencySet("org.jooq:3.18.6") {
            entry("jooq")
            entry("jooq-kotlin")
            entry("jooq-meta")
            entry("jooq-meta-extensions")
            entry("jooq-codegen")
            entry("jooq-checker")
            entry("jooq-postgres-extensions")
            entry("jooq-jackson-extensions")
            entry("jooq-meta-extensions-liquibase")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.zalando:logbook-spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    implementation("org.zalando:logbook-spring-boot-starter")
    implementation("org.jooq:jooq")
    implementation("org.jooq:jooq-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.h2database:h2")
    jooqGenerator("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    schemaPaths = mutableListOf("$projectDir/src/main/resources/schema.graphqls")
    packageName = "com.productboard.dgsdemo.dgs"
    language = "kotlin"
    generateClient = true
    generateKotlinNullableClasses = true
    generateKotlinClosureProjections = true
    typeMapping = mutableMapOf(
        "ISO8601DateTime" to "java.time.LocalDateTime",
    )
}

sourceSets {
    main {
        kotlin {
            srcDir("$buildDir/generated/sources/jooq")
        }
    }
}

jooq {
    version.set("3.18.6")  // the default (can be omitted)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // the default (can be omitted)/ the default (can be omitted)
    configurations {
        create("main") {  // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.h2.Driver"
                    url = "jdbc:h2:mem:dgsdemo-generation;INIT=RUNSCRIPT FROM 'src/main/resources/schema.sql'"
                    user = "sa"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                        inputSchema = "PUBLIC"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "com.productboard.dgddemo.jooq"
                        directory = "build/generated/sources/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

ktfmt {
    kotlinLangStyle()
    removeUnusedImports.set(true)
    maxWidth.set(120)
}

