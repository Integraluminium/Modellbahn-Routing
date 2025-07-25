plugins {
    id("java")
}

group = "de.dhbw.modellbahn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.2")

    testImplementation("org.graphstream:gs-core:2.0")
    testImplementation("org.graphstream:gs-ui-swing:2.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
}

tasks.test {
    useJUnitPlatform()
}