package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            stage('Download Ansible Playbooks') {
                git branch: 'master',
                    url: 'https://github.com/R-dVL/ansible-playbooks.git'
            }
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Project to deploy
            Project prj = new Project(this, 'lima-backend', '1.1.0')

            stage('Execute Playbook') {
                ansiblePlaybook(
                    inventory:'./inventories/hosts.yaml',
                    playbook: "./playbooks/deploy-backend.yaml",
                    credentialsId: 'jenkins',
                    extras: "-e ${prj}")
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}