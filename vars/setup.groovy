package com.rdvl.jenkinsLibrary

//// COMMON STAGE ////
// Setups Pipeline

def call() {
    try {
        stage('Setup') {
            cleanWs()

            // Configuration
            if(configuration) {
                String configurationJson = libraryResource resource: 'configuration.json'
                configuration = readJSON text: configurationJson
            }

            // Project to deploy
            if(project) {
                project = new Project(this, NAME, VERSION)
                project.init()
            }

            // Host
            if(host) {
                host = new Host(this, 'server')
                host.init()
            }

            // Donwload Ansible Playbooks
            git branch: 'master',
                url: 'https://github.com/R-dVL/ansible-playbooks.git'
        }
    } catch(Exception err) {
        error(err.getMessage())
    }
}