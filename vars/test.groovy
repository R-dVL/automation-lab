package com.rdvl.jenkinsLibrary

def call() {
    node () {
        ansiColor('xterm') {
            try {
                stage('Test') {
                    echo 'test'
                }
            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}