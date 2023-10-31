package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            stage('Test') {
                echo 'TEST'
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}