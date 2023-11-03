package com.rdvl.jenkinsLibrary

public class TechMVN {
    // Pipeline Context
    private def steps

    // Tech
    private String name
    private String version
    private String artifactId
    private String url
    private String tech

    TechMVN(steps, name, version, artifactId, url) {
        this.steps = steps
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.url = url
    }

    def prepare() {
        // Download Code
        steps.checkout(scm: [$class: 'GitSCM', userRemoteConfigs: [[url: url, credentialsId: 'github-login-credentials']], branches: [[name: version]]],poll: false)

        // Set-up Maven
        def mvnHome = steps.tool name: 'Maven 3.9.4', type: 'maven'
        def mvnCmd = "${mvnHome}/bin/mvn"

        // Protect token
        def settingsXml = """
        <settings>
            <servers>
                <server>
                    <id>github</id>
                    <username>${steps.github_user}</username>
                    <password>${steps.github_token}</password>
                </server>
            </servers>
        </settings>
        """
        steps.writeFile file: "${steps.WORKSPACE}/.m2/settings.xml", text: settingsXml

        // Build Artifact
        steps.sh "${mvnCmd} clean package"

        // Upload Artifact
        try {
            steps.sh "${mvnCmd} -Dmaven.package.skip=true deploy --settings ${steps.WORKSPACE}/.m2/settings.xml"

        } catch(Exception e) {
            steps.println('Artifact already uploaded to Github.')
        }
    }

    def deploy() {
        steps.host.sshCommand("""mkdir -p /opt/apps/${name}/${version}
        cd /opt/apps/${name}/${version}/
        curl -O -L https://_:${steps.github_token}@maven.pkg.github.com/R-dVL/${name}/com/rdvl/${name}/${version}/${artifactId}.jar
        /opt/apps/${name}/start.sh ${version}
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
