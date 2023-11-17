package com.rdvl.jenkinsLibrary

//// COMMON STAGE ////
// Setups Pipeline

def call(steps) {
    try {
        stage('Setup') {
            cleanWs()

            // Configuration
            if(steps.configuration) {
                String configurationJson = libraryResource resource: 'configuration.json'
                steps.configuration = readJSON text: configurationJson
            }

            // Project to deploy
            if(steps.project) {
                steps.project = new Project(this, NAME, VERSION)
                steps.project.init()
            }

            // Host
            if(steps.host) {
                steps.host = new Host(this, 'server')
                steps.host.init()
            }

            // Donwload Ansible Playbooks
            git branch: 'master',
                url: 'https://github.com/R-dVL/ansible-playbooks.git'
        }
    } catch(Exception err) {
        error(err.getMessage())
    }
}