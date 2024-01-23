package com.rdvl.jenkinsLibrary

def call() {
    node () {
        ansiColor('xterm') {
            environment {
                image
            }
            try {
                stage('Build image') {
                    git 'https://github.com/r-dvl/lima-backend.git'
                    image = docker.build('custom-jenkins:test')
                }

                stage('Push image') {
                    def (user, password) = utils.retrieveCredentials('github-user-password')
                    docker.withRegistry('github') {
                        image.push()
                    }
                }
            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}