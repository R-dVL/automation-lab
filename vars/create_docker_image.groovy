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

                    String sshKey = libraryResource resource: 'keys/jenkins_agent_key.pub'
                    writeFile file: 'jenkins_agent_key.pub', text: sshKey
                }
            }

            stage('Create and upload image') {
                String imageName = 'jenkins-agent'
                String imageTag = 'latest'
                withCredentials([
                    string(credentialsId: 'github-package-token', variable: 'token')]) {
                        script {
                            docker.withRegistry('https://docker.pkg.github.com', token) {
                                def customImage = docker.build('R-dVL/jenkins-agent:latest', '.')
                                customImage.push()
                            }
                        }
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}