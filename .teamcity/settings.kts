import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.project
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.githubConnection
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.version

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be registered
inside the project using the vcsRoot(), buildType(), template(),
and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2021.1"

project {
    vcsRoot(MasterBranchVcsRoot)
    vcsRoot(GradleBranchVcsRoot)

    buildType(TesterumApiMaster)
    buildType(TesterumApiGradle)

    features {
        githubConnection {
            displayName  = "GitHub connection"
            clientId     = DslContext.getParameter("TEAMCITY_GITHUB_CLIENT_ID")
            clientSecret = DslContext.getParameter("TEAMCITY_GITHUB_CLIENT_SECRET")
        }
    }
}

object TesterumApiMaster : BuildType({
    id("master")
    name = "master"

    vcs {
        root(MasterBranchVcsRoot)
    }

    steps {
        maven {
            name = "maven"
            goals = "clean package"
        }
    }

    triggers {
        trigger(TesterumApiVcsTrigger)
    }
})

object TesterumApiGradle : BuildType({
    id("gradle")
    name = "gradle"

    vcs {
        root(GradleBranchVcsRoot)
    }

    steps {
        gradle {
            tasks = "build"
            enableStacktrace = true
        }
    }

    triggers {
        trigger(TesterumApiVcsTrigger)
    }
})

object MasterBranchVcsRoot : TesterumApiBranchVcsRoot({
    id("MasterBranchVcsRoot")
    name = "testerum-api (master branch)"
    branch = "refs/heads/master"
})

object GradleBranchVcsRoot : TesterumApiBranchVcsRoot({
    id("GradleBranchVcsRoot")
    name = "testerum-api (gradle branch)"
    branch = "refs/heads/gradle"
})

open class TesterumApiBranchVcsRoot(init: GitVcsRoot.() -> Unit) : GitVcsRoot({
    url = "git@github.com:testerum/testerum-api.git"

    authMethod = uploadedKey {
        this.uploadedKey = "teamcity@github-testerum-api"
    }

    this.init()
})

object TesterumApiVcsTrigger : VcsTrigger({})
