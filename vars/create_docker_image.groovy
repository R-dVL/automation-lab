package com.rdvl.jenkinsLibrary

def call() {
    node {
        // Environment variables
        environment {
            configuration
        }
        // Pipeline error control
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            stage('Get Dockerfile') {
                script {
                    String dockerfile = libraryResource resource: 'Dockerfile'
                    writeFile file: 'Dockerfile', text: dockerfile
                }
            }

            stage('Create and upload image') {
                String imageName = 'jenkins-agent'
                String imageTag = 'latest'
                script {
                    sh """
                    docker build -t ${imageName}:${imageTag} .
                    docker tag ${imageName}:${imageTag} ghcr.io/R-dVL/${imageName}:${imageTag}
                    docker login ghcr.io -u R-dVL
                    docker push ghcr.io/R-dVL/${imageName}:${imageTag}
                    """
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}