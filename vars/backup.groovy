package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
                project
                host
            }
            try {
                stage('Setup') {
                    cleanWs()

                    // Donwload Ansible Playbooks
                    git branch: 'master',
                        url: 'https://github.com/R-dVL/ansible-playbooks.git'
                }

                stage('Backup') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/test.yaml",
                        credentialsId: 'server-credentials',
                        colorized: true,
                        extras: "-v")
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}