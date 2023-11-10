package com.rdvl.jenkinsLibrary

def call() {
    node ('server') {
        try {
            stage('Get Dockerfile') {
                script {
                    String dockerfile = libraryResource resource: 'Dockerfile'
                    writeFile file: 'Dockerfile', text: dockerfile
                }
            }

            stage('Build') {
                script {
                    sh ("docker build -t jenkins-agent:latest .")
                }
            }

            stage('Login') {
                withCredentials([
                    string(credentialsId: 'github-package-token	', variable: 'token')]) {
                        script {
                            sh "echo ${token} | docker login ghcr.io -u r-dvl --password-stdin"
                        }
                    }
            }

            stage('Push') {
                script {
                    sh "docker push ghcr.io/r-dvl/jenkins-library/jenkins-agent:latest"
                }
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}