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
        // Stop service
        pipeline.host.sshCommand("pm2 stop ${name}", true)

        // Deploy
        pipeline.host.sshCommand("""mkdir /opt/apps/${name}/${version}
        cd /opt/apps/${name}/${version}
        git clone --depth 1 --branch ${version} ${url}
        cd ${name}
        npm install
        """)

        // Start service
        pipeline.host.sshCommand("/opt/apps/${name}/start.sh ${version}", true)

        // Show log
        pipeline.host.sshCommand("cat /opt/apps/${name}/${version}/${name}.log")
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
