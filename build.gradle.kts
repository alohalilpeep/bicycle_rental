plugins {
    id("java")
    id("application")
    id("maven-publish")  // Already included
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClass.set("org.example.Main")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            // Customize POM metadata (optional)
            pom {
                name.set("My Java Library")
                description.set("A library published to GitHub Packages")
                url.set("https://github.com/alohalilpeep/bicycle_rental")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/alohalilpeep/bicycle_rental")
            credentials {
                username = System.getenv("alohalilpeep")
                password = System.getenv("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")
            }
        }
    }
}