package com.rdvl.jenkinsLibrary

def call() {
    node () {
        environment {
            project
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

                stage('Build') {
                    project.getTechnology().build()
                }

                stage('Publish') {
                    project.getTechnology().publish()
                }

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}