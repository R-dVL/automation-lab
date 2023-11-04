package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        environment {
            configuration
            host
        }
        try {
            // Configuration
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Project to deploy
            Project prj = new Project(this, NAME, VERSION)

            // Host Setup
            host = new Host(this, HOST)
            host.init()

            stage('Prepare') {
                cleanWs()
                prj.getDeploymentTech().prepare()
            }

            stage('Deploy') {
                prj.getDeploymentTech().deploy()
            }

        } catch(Exception e) {
            error(e.getMessage())
        }
    }
}