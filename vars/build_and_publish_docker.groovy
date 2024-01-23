package com.rdvl.jenkinsLibrary

def call() {
    node () {
        environment {
            project
            image
        }
        ansiColor('xterm') {
            try {
                stage('Setup') {
                    project = new Project(this, PROJECT_NAME, TAG)
                    git "${project.getUrl()}"
                }

                stage('Build image') {
                    image = docker.build("ghcr.io/r-dVl/${project.getArtifactName()}:${TAG}")
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