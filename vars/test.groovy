package com.rdvl.jenkinsLibrary

def call() {
    node () {
        try {
            stage('Test') {
                git 'https://github.com/r-dvl/lima-backend.git'
                def customImage = docker.build('custom-jenkins:latest')
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}