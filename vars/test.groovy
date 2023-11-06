package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            sshagent(credentials: ['jenkins']) {
            sh 'ls -altr'
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}