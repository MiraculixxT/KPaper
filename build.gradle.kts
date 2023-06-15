import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val githubRepo = "MiraculixxT/KPaper"

group = "de.miraculixx"
version = "1.20.1"

description = "A Kotlin API for Minecraft plugins using the Paper toolchain"

plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.20"

    `java-library`
    `maven-publish`
    signing

    id("org.jetbrains.dokka") version "1.8.20"

    id("io.papermc.paperweight.userdev") version "1.5.3"
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    dokkaHtml.configure {
        outputDirectory.set(projectDir.resolve("docs"))
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

signing {
    sign(publishing.publications)
}

publishing {
    repositories {
        maven("https://oss.sonatype.org/service/local/staging/deploy/maven2") {
            name = "ossrh"
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        register<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks.jar.get().outputs.files.single())

            this.groupId = project.group.toString()
            this.artifactId = project.name.toLowerCase()
            this.version = project.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)

                developers {
                    developer {
                        name.set("miraculixx")
                        //TODO possible to add multiple devs? names.add() do not work
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License, Version 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                    }
                }

                url.set("https://github.com/${githubRepo}")

                scm {
                    connection.set("scm:git:git://github.com/${githubRepo}.git")
                    url.set("https://github.com/${githubRepo}/tree/main")
                }
            }
        }
    }
}
