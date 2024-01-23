package com.rdvl.jenkinsLibrary

def call() {
    node () {
        environment {
            project
            image
            configuration
        }
        ansiColor('xterm') {
            try {
                stage('Setup') {
                    // Read configuration file
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))

                    // Init project
                    project = new Project(this, PROJECT_NAME, TAG)

                    // Clone project repository
                    git "${project.getUrl()}"
                }

                stage('Build image') {
                    image = docker.build("ghcr.io/r-dvl/${project.getArtifactName()}:${TAG}")
                }

                stage('Push image') {
                    docker.withRegistry('https://ghcr.io', 'github-user-password') {
                        image.push()
                    }
                }
            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}