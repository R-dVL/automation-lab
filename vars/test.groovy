package com.rdvl.jenkinsLibrary


def call() {
    node ('docker-agent') {
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

                // Common functions
                def connectivity_test = load 'common/connectivity_test.groovy'
                connectivity_test.call(host)

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