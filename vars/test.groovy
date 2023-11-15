package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            try {
                stage('Setup') {
                    cleanWs()
                    // Donwload Ansible Playbooks
                    git branch: 'master',
                        url: 'https://github.com/R-dVL/ansible-playbooks.git'
                }

                stage('Test') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/backup.yaml",
                        credentialsId: 'jenkins',
                        colorized: true)
                }

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}