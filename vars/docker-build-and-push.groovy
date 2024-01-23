package com.rdvl.jenkinsLibrary

def call() {
    node () {
        ansiColor('xterm') {
            try {
                stage('Build image') {
                    image = docker.build("ghcr.io/${USER}/${IMAGE_NAME}:${TAG}")
                }

                stage('Push image') {
                    docker.withRegistry('https://ghcr.io') {
                        image.push()
                    }
                }
            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}