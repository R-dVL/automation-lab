package com.rdvl.jenkinsLibrary

def call() {
    node () {
        env {
            image
        }
        try {
            stage('Build image') {
                git 'https://github.com/r-dvl/lima-backend.git'
                image = docker.build('custom-jenkins:test')
            }

            stage('Push image') {
                def (user, password) = retrieveCredentials('github-user-password')
                docker.withRegistry('https://ghcr.io', "docker login -u ${user} -p ${password}") {
                    image.push()
                }
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}