plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "com.testerum"
    version = project.rootProject.properties["releaseVersion"] ?: "develop-SNAPSHOT"
}

nexusPublishing {
    repositories {
        sonatype()
    }
}
