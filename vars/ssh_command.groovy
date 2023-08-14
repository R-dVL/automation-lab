package com.github.rdvl.automationLibrary

import groovy.json.JsonSlurperClassic

def call() {
    node {
        // Environment variables
        environment {
            constants
            configuration
        }
        // Pipeline error control
        try {
            // Constants instance
            constants = Constants.getInstance()
            // Clean before build
            cleanWs()
            // Clone Repo
            git(
                url: "${constants.repoURL}",
                credentialsId: 'github-token',
                branch: 'master'
            )
            // Retrieve Configuration
            def jsonSlurperClassic = new JsonSlurperClassic()
            configuration = jsonSlurperClassic.parse(new File("${WORKSPACE}${constants.configPath}"))
            // Default Params
            Host host = new Host(this, 'Server')
            // Build Name
            currentBuild.displayName = "SSH Command - " + HOST
            currentBuild.description = CMD

            stage('Host Setup') {
                script {
                    // Retrieve info from Jenkins
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

            stage('Execute Command'){
                host.sshCommand(CMD)
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            println("ERROR | Message: ${err.getMessage()}")
            error(err)
        }
    }
}