package com.rdvl.jenkinsLibrary


def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
                host
                log
            }
            try {
                stage('Setup') {
                    // Configuration instance
                    String configurationJson = libraryResource resource: 'configuration.json'
                    configuration = readJSON text: configurationJson

                    // Default Params
                    host = new Host(this, HOST)
                    host.init()
                }

                connectivity_test(host)

                stage('Execute Command') {
                    def result = host.sshCommand(CMD, SUDO)
                    print("Result: ${result}")
                }

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}