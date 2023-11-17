package com.rdvl.jenkinsLibrary


def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
                host
            }
            try {
                setup(this)
                connectivity_test(host)

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}