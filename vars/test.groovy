package com.rdvl.jenkinsLibrary

def call() {
    node () {
        try {
            stage('Test') {
                echo 'test'
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}