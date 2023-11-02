package com.rdvl.jenkinsLibrary


def call() {
    node ('docker-agent') {
        // Environment variables
        environment {
            configuration
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Default Params
            host = new Host(this, HOST)

            stage('Execute Command') {
                host.sshCommand(CMD, SUDO)
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}