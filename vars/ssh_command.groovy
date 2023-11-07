package com.rdvl.jenkinsLibrary


def call() {
    node ('docker-agent') {
        try {
            stage('Setup') {
                sshagent(credentials: ['jenkins']) {
                    sh(CMD)
                }
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}