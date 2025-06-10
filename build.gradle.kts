plugins {
    id("java")
    id("application")  // Add this for running the application
    id("maven-publish")
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])  // Публикует JAR-файл
            // Дополнительные метаданные (опционально)
            pom {
                name.set("Bicycle Rental")
                description.set("Library for bicycle rental management")
                url.set("https://github.com/alohalilpeep/bicycle_rental")
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/alohalilpeep/bicycle_rental")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

