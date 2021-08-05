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
            val sonatypeNexusUsername: String? = System.getenv("SONATYPE_NEXUS_USERNAME")
            val sonatypeNexusPassword: String? = System.getenv("SONATYPE_NEXUS_PASSWORD")

            username.set(sonatypeNexusUsername)
            password.set(sonatypeNexusPassword)
        }
    }
}
