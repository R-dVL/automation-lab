package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            try {
                stage('TEST') {
                    utils.log('ola en rojo', 'red')
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}