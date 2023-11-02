package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        environment {
            configuration
            mongo_user
            mongo_password
            host
        }
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Default Params
            Project prj = new Project(this, NAME, VERSION)
            host = new Host(this, HOST)

            // TODO: Retrieve host credentials in Host constructor
            stage('Retrieve Credentials') {
                script {
                    // Host User & Password
                    withCredentials([usernamePassword(credentialsId: host.getConfigCredentials(), usernameVariable: 'user', passwordVariable: 'password')]) {
                        host.setUser(user)
                        host.setPassword(password)
                    }
                    // Mongodb User & Password
                    withCredentials([usernamePassword(credentialsId: 'mongo-credentials', usernameVariable: 'user', passwordVariable: 'password')]) {
                        mongo_user = user
                        mongo_password = password
                    }
                }
            }

            stage('Prepare') {
                cleanWs()
                prj.getDeploymentTech().prepare()
            }

            stage('Deploy') {
                prj.getDeploymentTech().deploy()
            }

        } catch(Exception e) {
            error(e.getMessage())
        }
    }
}