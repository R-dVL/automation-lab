package com.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()

            // TODO: Retrieve host credentials in Host constructor
            stage('Host Setup') {
                git branch: 'master',
                    url: 'https://github.com/R-dVL/ansible-playbooks.git'
            }

            stage('Execute Playbook') {
                ansiblePlaybook playbook: 'hello-world.yaml'
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}