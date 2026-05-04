plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.github.MTRILogic"
version = "1.0.0"

java {
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    api("com.github.MTRILogic:SpringPanel2:1.0.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
        }
    }
}