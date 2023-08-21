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

        // Build
        pipeline.sh("npm install")

        // Upload Artifact
        pipeline.sh("npm config set //npm.pkg.github.com/:_authToken=${pipeline.github_token}")
        try {
            pipeline.sh("npm publish")
        } catch (Exception e) {
            pipeline.println('Artifact already uploaded to Github.')
        }
    }

    def deploy() {
        pipeline.host.sshCommand("""systemctl stop ${name}
        rm -rf apps/${name}
        cd apps/
        git clone --depth 1 --branch ${version} ${url}
        cd ${name}
        npm install
        systemctl start ${name}
        """)
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
