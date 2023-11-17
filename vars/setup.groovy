package com.rdvl.jenkinsLibrary

//// COMMON STAGE ////
// Setups Pipeline

def call() {
    try {
        stage('Setup') {
            cleanWs()

            // Configuration
            if(steps.env.configuration != null) {
                String configurationJson = libraryResource resource: 'configuration.json'
                steps.env.configuration = readJSON text: configurationJson
            }

            // Project to deploy
            if(steps.env.project != null) {
                steps.env.project = new Project(this, NAME, VERSION)
                steps.env.project.init()
            }

            // Host
            if(steps.env.host != null) {
                steps.env.host = new Host(this, 'server')
                steps.env.host.init()
            }

            // Donwload Ansible Playbooks
            git branch: 'master',
                url: 'https://github.com/R-dVL/ansible-playbooks.git'
        }
    } catch(Exception err) {
        error(err.getMessage())
    }
}