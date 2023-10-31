package com.rdvl.jenkinsLibrary

def call() {
    node ('Docker') {
        try {
            stage('Test') {
                echo 'TEST'
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}