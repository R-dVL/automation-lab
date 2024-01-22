package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for building and pushing a Docker image to GitHub Container Registry.
 *
 * This pipeline retrieves a Dockerfile from the Jenkins library, builds a Docker image,
 * logs in to GitHub Container Registry, and pushes the Docker image.
 */
def call() {
    node ('server') {
        ansiColor('xterm') {
            try {
                stage('Get Dockerfile') {
                    script {
                        String dockerfile = libraryResource resource: 'Dockerfile'
                        writeFile file: 'Dockerfile', text: dockerfile
                    }
                }

                stage('Build') {
                    script {
                        sh ("docker build -t ghcr.io/r-dvl/jenkins-agent:latest .")
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
                        sh "docker push ghcr.io/r-dvl/jenkins-agent:latest"
                    }
                }

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}