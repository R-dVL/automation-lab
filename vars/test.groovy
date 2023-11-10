package com.rdvl.jenkinsLibrary

def call() {
    node ('server') {
        environment {
            user
        }
        try {
            user = 'r-dvl'

            stage('Get Dockerfile') {
                script {
                    String dockerfile = libraryResource resource: 'Dockerfile'
                    writeFile file: 'Dockerfile', text: dockerfile
                }
            }

            stage('Build') {
                script {
                    sh ("docker build -t ghcr.io/${user}/jenkins-library/jenkins-agent:latest .")
                }
            }

            stage('Login') {
                withCredentials([
                    string(credentialsId: 'github-package-token	', variable: 'token')]) {
                        script {
                            sh "echo ${token} | docker login ghcr.io -u ${user} --password-stdin"
                        }
                    }
            }

            stage('Push') {
                script {
                    sh "docker push ghcr.io/${user}/jenkins-library/jenkins-agent:latest"
                }
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}