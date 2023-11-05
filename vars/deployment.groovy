package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
            }
            try {
                // Configuration
                String configurationJson = libraryResource resource: 'configuration.json'
                configuration = readJSON text: configurationJson

                // Project to deploy
                Project project = new Project(this, NAME, VERSION)
                project.init()

                stage('Prepare') {
                    // TODO: Tests and Sonar
                }

                stage('Deploy') {
                    def credentials = utils.retrieveCredentials('mongo-credentials')
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/${project.getPlaybook()}.yaml",
                        credentialsId: 'jenkins',
                        colorized: true,
                        extras: "-e ${project.getProjectJson()} -v")
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}