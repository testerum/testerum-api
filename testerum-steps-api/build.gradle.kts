import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

description = "Public API used to extend the Testerum test automation platform."

plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

// Java
val javaVersion = "1.8"
java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = sourceCompatibility
}

// Kotlin
tasks.withType<KotlinCompile> {
    kotlinOptions {
        allWarningsAsErrors = true
        apiVersion = "1.4"
        languageVersion = "1.4"
        jvmTarget = javaVersion
        freeCompilerArgs = listOf(
            "-progressive"
        )
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

val isRelease = project.hasProperty("release")
if (isRelease) {
    // produce sources jar
    java {
        withSourcesJar()
    }

    // produce empty javadocJar
    val javadocJar = tasks.register<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
    }
    tasks.assemble {
        dependsOn(javadocJar)
    }

    publishing {
        publications {
            create<MavenPublication>(project.name) {
                from(components["kotlin"])
                artifact(tasks.named("kotlinSourcesJar"))
                artifact(javadocJar)

                pom {
                    name.set(project.name)
                    description.set(project.description)

                    url.set("https://testerum.com/")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            name.set("Vasile-Cristian Mocanu")
                            email.set("cristian.mocanu@testerum.com")
                            organization.set("Testerum")
                            organizationUrl.set("https://testerum.com/")
                        }

                        developer {
                            name.set("Ionut Pruteanu")
                            email.set("ionut.pruteanu@testerum.com")
                            organization.set("Testerum")
                            organizationUrl.set("https://testerum.com/")
                        }
                    }

                    scm {
                        url.set("https://github.com/testerum/testerum-api")
                    }
                }
            }
        }
    }

    signing {
        val signingKey: String = getRequiredEnv("SIGNING_KEY")
        val signingPassword: String = getRequiredEnv("SIGNING_PASSWORD")

        @Suppress("UnstableApiUsage")
        useInMemoryPgpKeys(signingKey, signingPassword)

        // sign all publications
        for (publication in project.publishing.publications) {
            sign(publication)
        }

        // make publish tasks depend on sign tasks
        for (repository in project.publishing.repositories) {
            for (publication in project.publishing.publications) {
                val publishTaskName = "publish${publication.name.capitalize()}PublicationTo${repository.name.capitalize()}Repository"
                val signTaskName = "sign${publication.name.capitalize()}Publication"

                tasks.named(publishTaskName) {
                    dependsOn(tasks.named(signTaskName))
                }
            }
        }
    }
}

fun getRequiredEnv(key: String): String {
    return System.getenv(key)
        ?: throw RuntimeException("missing required environment variable [$key]")
}
