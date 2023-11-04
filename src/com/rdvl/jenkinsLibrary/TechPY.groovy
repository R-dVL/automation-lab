package com.rdvl.jenkinsLibrary

public class TechPY {
    // Pipeline Context
    private def steps

    // Tech
    private String name
    private String version
    private String artifactId
    private String url
    private String tech

    // Control params
    private boolean allreadyDeployed

    TechPY(steps, name, version, artifactId, url) {
        this.steps = steps
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.url = url
    }

    def prepare() {
        // Prepare
        def result = steps.host.sshCommand("if [ -d \'/opt/apps/${name}/${version}\' ]; then echo \'true\'; else echo \'false\'; fi").trim()

        this.allreadyDeployed = result.toBoolean()
    }

    def deploy() {
        // Stop service
        try { steps.host.sshCommand("pm2 stop ${name}", true) } catch (Exception e) { steps.println('Already stopped..') }

        // Deploy
        if(this.allreadyDeployed == false) {
            steps.host.sshCommand("""mkdir /opt/apps/${name}/${version}
            cd /opt/apps/${name}/${version}
            git clone --depth 1 --branch ${version} ${url}
            cd ${name}
            python3 -m venv venv
            source venv/bin/activate
            pip3 install -r requirements.txt
            """)

            // Write .env file
            def mongoCredentials = steps.utils.retrieveCredentials(credentialsId)
            def env =
            """MONGO_USER=${mongoCredentials.user}
            MONGO_PASSWORD=${mongoCredentials.password}
            MONGO_URI=192.168.1.55:27017
            MONGO_DB=cat-watcher
            """

            steps.writeFile file: "./.env", text: env

            // Send .env file
            steps.host.sshPut('./.env', "/opt/apps/${name}/${version}/${name}")

        } else {
            steps.print('Allready deployed, starting service..')
        }

        // Start service
        steps.host.sshCommand("bash /opt/apps/${name}/start.sh ${version}", true)

        // Wait until service is started
        steps.sleep(15)

        // Show log
        steps.host.sshCommand("cat /opt/apps/${name}/${version}/${name}.log")
    }

    @Override
    @NonCPS
    public String toString() {
        return """Name: ${name}
            Version: ${version}
            Artifact: ${artifactId}
            Url: ${url}
            Destination: ${destination}
            Tech: ${tech}
        """;
    }
}
