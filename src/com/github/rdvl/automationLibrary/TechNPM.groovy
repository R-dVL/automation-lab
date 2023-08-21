package com.github.rdvl.automationLibrary;

public class TechNPM {
    // Pipeline Context
    private def pipeline

    // Tech
    private String name
    private String version
    private String artifactId
    private String nexusRepository
    private String url
    private def destination
    private String tech

    TechNPM(pipeline, name, version, artifactId, nexusRepository, url, destination) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.nexusRepository = nexusRepository
        this.url = url
        this.destination = destination
    }

    def prepare() {
        // Download Code
        pipeline.checkout(scm: [$class: 'GitSCM', userRemoteConfigs: [[url: url, credentialsId: 'github-login-credentials']], branches: [[name: version]]],poll: false)

        // Config .npmrc file
        String npmrc = """//npm.pkg.github.com/:username=${pipeline.github_user}
        //npm.pkg.github.com/:_password=${pipeline.github_token}
        //npm.pkg.github.com/:email=rauldel.valle.lima@hotmail.com
        //npm.pkg.github.com/:always-auth=true
        """

        pipeline.writeFile file: "${pipeline.WORKSPACE}/.npmrc", text: npmrc
        pipeline.sh("npm publish --registry=https://npm.pkg.github.com")
    }

    def deploy() {

    }

    @Override
    @NonCPS
    public String toString() {
        return """
            Name: ${name}
            Version: ${version}
            Artifact: ${artifactId}
            Url: ${url}
            Destination: ${destination}
            Tech: ${tech}
        """;
    }
}
