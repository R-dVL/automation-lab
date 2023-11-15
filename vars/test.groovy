package com.rdvl.jenkinsLibrary

def call() {
    node ('server') {
        try {
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