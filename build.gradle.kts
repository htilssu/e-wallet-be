plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}
val springCloudVersion by extra("2023.0.2")

group = "com.ewallet"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.auth0:java-jwt:2.0.1")
    implementation("org.springframework.cloud:spring-cloud-function-context")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("io.projectreactor:reactor-test")

    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

