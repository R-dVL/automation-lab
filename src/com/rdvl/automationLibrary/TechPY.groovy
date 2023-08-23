package com.rdvl.automationLibrary;

public class TechPY {
    // Pipeline Context
    private def pipeline

    // Tech
    private String name
    private String version
    private String artifactId
    private String url
    private String tech

    // Control params
    private boolean allreadyDeployed

    TechPY(pipeline, name, version, artifactId, url) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.url = url
    }

    def prepare() {
        // Prepare
        def result = pipeline.host.sshCommand("if [ -d \'/opt/apps/${name}/${version}\' ]; then return \'true\'; else return \'false\'; fi")

        pipeline.print(result)
        pipeline.print(result.getClass())

        this.allreadyDeployed = result    
    }

    def deploy() {
        // Stop service
        try { pipeline.host.sshCommand("pm2 stop ${name}", true) } catch (Exception e) { pipeline.println('Already stopped..') }

        // Deploy
        pipeline.print(allreadyDeployed)
        pipeline.print(this.allreadyDeployed)
        if(this.allreadyDeployed == 'false') {
            pipeline.host.sshCommand("""mkdir /opt/apps/${name}/${version}
            cd /opt/apps/${name}/${version}
            git clone --depth 1 --branch ${version} ${url}
            cd ${name}
            python3 -m venv venv
            source venv/bin/activate
            pip3 install -r requirements.txt
            """)

            // Write .env file
            def env = """MONGO_USER=${pipeline.mongo_user}
    MONGO_PASSWORD=${pipeline.mongo_password}
    MONGO_URI=192.168.1.55:27017
    MONGO_DB=cat-watcher
            """
            pipeline.writeFile file: "./.env", text: env

            // Send .env file
            pipeline.host.sshPut('./.env', "/opt/apps/${name}/${version}/${name}")

        } else {
            pipeline.print('Allready deployed, starting service..')
        }

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
