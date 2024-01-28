package com.rdvl.jenkinsLibrary

def call(project_name, version) {
    node () {
        environment {
            project
            configuration
            buildError
        }
        ansiColor('xterm') {
            try {
                stage('Prepare') {
                    cleanWs()    //Clean Workspace
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))    // Read configuration file
                    project = new Project(this, project_name, version)    // Init project

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

            } catch(Exception e) {
                buildError = e.getMessage()
                error(buildError)

            } finally {
                switch(currentBuild.currentResult) {
                    case 'SUCCESS':
                        String title = "${JOB_NAME} - SUCCESS"
                        String message = "Project: ${project_name} - Version: ${version}"
                        utils.notification(title, message)
                        break
                    
                    case 'FAILURE':
                        String title = "${JOB_NAME} - FAILED"
                        String message = "${buildError}"
                        utils.notification(title, message)
                        break
                    
                    case 'UNSTABLE':
                        String title = "${JOB_NAME} - UNSTABLE"
                        String message = "Build unstable"
                        utils.notification(title, message)
                        break

                    default:
                        String title = "${JOB_NAME}"
                        String message = "Unknown result"
                        utils.notification(title, message)
                        break
                }
            }
        }
    }
}