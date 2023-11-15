package com.rdvl.jenkinsLibrary


def call() {
    node ('docker-agent') {
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
                host = new Host(this, HOST)
                host.init()
            }

            stage('Execute Command') {
                def result = host.sshCommand(CMD, SUDO)
                print result
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}