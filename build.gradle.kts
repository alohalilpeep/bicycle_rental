plugins {
    id("java")
    id("application")  // Add this for running the application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0")) // BOM for version management
    testImplementation("org.junit.jupiter:junit-jupiter-api") // API for writing tests
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine") // Engine to run tests
}

application {
    mainClass.set("org.example.Main")  // Configure the main class
}

tasks.test {
    useJUnitPlatform()
}

