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

            stage('Build') {
                script {
                    sh ("docker build -t rdvlima/jenkins-agent:latest .")
                }
            }

            stage('Login') {
                withCredentials([
                    usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'user', passwordVariable: 'password')]) {
                        script {
                            sh "echo ${password} | docker login -u ${user} --password-stdin"
                        }
                    }
            }

            stage('Push') {
                script {
                    sh 'docker push rdvlima/jenkins-agent:latest'
                }
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}