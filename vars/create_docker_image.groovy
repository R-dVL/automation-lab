package com.rdvl.jenkins-library

def call() {
    node {
        // Environment variables
        environment {
            cfg
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()

            stage('Get Dockerfile') {
                script {
                    String dockerfile = libraryResource resource: 'Dockerfile'
                    writeFile file: 'Dockerfile', text: dockerfile
                }
            }

            stage('Create and upload image') {
                script {
                    sh """
                    docker build -t jenkins-library:latest .
                    docker tag jenkins-library:latest ghcr.io/R-dVL/jenkins-library:latest
                    docker login ghcr.io -u R-dVL
                    docker push ghcr.io/R-dVL/jenkins-library:latest
                    """
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}