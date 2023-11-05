package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            stage('Download Ansible Playbooks') {
                git branch: 'master',
                    url: 'https://github.com/R-dVL/ansible-playbooks.git'
            }

            def host = 'jenkins'

            stage('Execute Playbook') {
                ansiblePlaybook(
                    inventory:'./inventories/hosts.yaml',
                    playbook: "./playbooks/hello-world.yaml",
                    credentialsId: 'jenkins',
                    extras: "-e host=${host}")
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}