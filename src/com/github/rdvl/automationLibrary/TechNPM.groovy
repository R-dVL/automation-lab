package com.github.rdvl.automationLibrary;

public class TechNPM {
    // Pipeline Context
    private def pipeline

    // Tech
    private String name
    private String version
    private String artifactId
    private String url
    private String tech

    TechNPM(pipeline, name, version, artifactId, url) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.url = url
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
        try {
            pipeline.host.sshCommand("pm2 stop ${name}", true)

        } catch (Exception e) {
            pipeline.println('Service already stopped.')
        }

        // Deploy
        pipeline.host.sshCommand("""mkdir /opt/apps/${name}/${version}
        cd /opt/apps/${name}/${version}
        git clone --depth 1 --branch ${version} ${url}
        cd ${name}
        npm install
        """)

        // Start service
        pipeline.host.sshCommand("bash /opt/apps/${name}/start.sh ${version}", true)

        // Wait until service is started
        pipeline.sleep(15)

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
