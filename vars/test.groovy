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

                def connectivity_test = load(libraryResource(resource: 'common/connectivity_test.groovy'))
                connectivity_test.call(host)

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}