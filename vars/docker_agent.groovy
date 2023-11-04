package com.rdvl.jenkinsLibrary


def call() {
    node ('server') {
        environment {
            configuration
            host
        }
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Default Params
            host = new Host(this, 'server')
            host.init()

            stage('Execute Command') {
                String command

                switch (OPTION) {
                    case 'ON':
                        String sshKey = libraryResource resource: 'keys/jenkins_agent_key.pub'
                        command = """
                            docker run -d --rm --name=jenkins-agent -p 4444:22 \
                            -e "JENKINS_AGENT_SSH_PUBKEY=${sshKey}" \
                            rdvlima/jenkins-agent
                        """
                        break

                    case 'OFF':
                        command = 'docker stop jenkins-agent'
                        break
                    
                    default:
                        error("${OPTION} not defined.")
                }

                host.sshCommand(command)

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}