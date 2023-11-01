package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        // Environment variables
        environment {
            configuration
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Default Params
            host = new Host(this, HOST)

            // TODO: Retrieve host credentials in Host constructor
            stage('Host Setup') {
                // Retrieve info from Jenkins
                script {
                    // User & Password
                    withCredentials([
                        usernamePassword(credentialsId: host.getConfigCredentials(), usernameVariable: 'user', passwordVariable: 'password')]) {
                            host.setUser(user)
                            host.setPassword(password)
                    }
                    // IP
                    withCredentials([
                        string(credentialsId: host.getConfigIp(), variable: 'ip')]) {
                            host.setIp(ip)
                    }
                }
            }

            stage('Execute Command') {
                host.sshCommand(CMD, SUDO)
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}