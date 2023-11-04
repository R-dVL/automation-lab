package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            stage('Download Ansible Playbooks') {
                git branch: 'master',
                    url: 'https://github.com/R-dVL/ansible-playbooks.git'
            }

            stage('Execute Playbook') {
                ansiblePlaybook(
                    inventory:'./inventories/hosts',
                    playbook: "./playbooks/hello-world.yaml",
                    credentialsId: 'jenkins')
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}