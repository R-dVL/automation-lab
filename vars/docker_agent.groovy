package com.rdvl.jenkinsLibrary


def call() {
    node ('server') {
        ansiColor('xterm') {
            environment {
                configuration
                host
            }
            try {
                stage('Setup') {
                    // Configuration instance
                    String configurationJson = libraryResource resource: 'configuration.json'
                    configuration = readJSON text: configurationJson

                    // Default Params
                    host = new Host(this, 'server')
                    host.init()
                }

                stage('Execute Command') {
                    String command

                    switch (OPTION) {
                        case 'ON':
                            String sshKey = libraryResource resource: 'keys/jenkins_agent_key.pub'
                            command = """
                                docker run -d --rm --name=jenkins-agent -p 4444:22 \
                                -e "JENKINS_AGENT_SSH_PUBKEY=${sshKey}" \
                                ghcr.io/r-dvl/jenkins-agent:latest
                            """
                            break

                        case 'OFF':
                            command = 'docker stop jenkins-agent'
                            break

                        default:
                            error("${OPTION} not defined.")
                    }
                    // Update Docker Image
                    host.sshCommand('docker pull ghcr.io/r-dvl/jenkins-agent:latest')
                    // Start Container
                    host.sshCommand(command)
                }

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}