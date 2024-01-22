package com.rdvl.jenkinsLibrary

def call() {
    node () {
        try {
            stage('Test') {
                print('test')
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}