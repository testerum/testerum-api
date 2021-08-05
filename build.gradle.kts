plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "com.testerum"
    version = project.rootProject.properties["releaseVersion"] ?: "develop-SNAPSHOT"
}

nexusPublishing {
    repositories {
        sonatype {
            val sonatypeNexusUsername: String = getRequiredEnv("SONATYPE_NEXUS_USERNAME")
            val sonatypeNexusPassword: String = getRequiredEnv("SONATYPE_NEXUS_PASSWORD")

            username.set(sonatypeNexusUsername)
            password.set(sonatypeNexusPassword)
        }
    }
}

fun getRequiredEnv(key: String): String {
    return System.getenv(key)
        ?: throw RuntimeException("missing required environment variable [$key]")
}
