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
                        inventory: "${WORKSPACE}/inventories/hosts.yaml",
                        playbook: "${WORKSPACE}/playbooks/backup.yaml",
                        credentialsId: 'server-credentials',
                        colorized: true,
                        extras: "-e path=${PATH} -vvv")
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}