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
                stage('Prepare') {
                    cleanWs()    //Clean Workspace
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))    // Read configuration file
                    project = new Project(this, PROJECT_NAME, TAG)    // Init project

                    // Clone project repository
                    checkout scmGit(
                        branches: [[name: "${project.getVersion()}"]],
                        userRemoteConfigs: [[url: "${project.getUrl()}"]]
                    )

                    currentBuild.displayName = "${project.getName()}:${project.getVersion()}"    // Build name
                }

                stage('Build image') {
                    image = docker.build("ghcr.io/r-dvl/${project.getArtifactName()}:${project.getVersion()}")
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