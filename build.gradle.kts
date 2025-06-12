plugins {
    id("java")
    id("application")
    id("maven-publish")  // Already included
}

group = "org.alohalilpeep"
version = "1.0.1"

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
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/alohalilpeep/bicycle_rental")
            credentials {
                username = project.findProperty("gpr.user") ?: System.getenv("alohalilpeep")
                password = project.findProperty("gpr.key") ?: System.getenv("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")
            }
        }
    }
    publications {
        gpr(MavenPublication) {
            from(components.java)
        }
    }
}