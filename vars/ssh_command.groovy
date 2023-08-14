package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Constants instance
            cfg = Configuration.getInstance()
            // Default Params
            Host host = new Host(this, HOST)
            // Build Name
            currentBuild.displayName = "SSH Command - " + HOST
            currentBuild.description = CMD

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
                host.sshCommand(CMD)
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            println("ERROR | Message: ${err.getMessage()}")
            error(err)
        }
    }
}