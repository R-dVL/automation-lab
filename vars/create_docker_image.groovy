package com.rdvl.jenkinsLibrary

def call() {
    node ('server') {
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

            stage('Build and Push Image') {
                String imageName = 'jenkins-agent'
                String imageTag = 'latest'
                withCredentials([
                    string (credentialsId: 'dockerhub-token', variable: 'token')]) {
                        script {
                            def customImage = docker.build('jenkins-agent:latest', '.')
                            customImage.push()
                        }
                }
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}