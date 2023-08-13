package com.github.rdvl.automationLibrary

import groovy.json.JsonSlurperClassic

def call() {
    node {
        environment {
            constants
            configuration
        }
        try {
            stage('Pipeline Setup') {
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
            }

            // Default Params
            Host host = new Host(this, 'RPi')

            currentBuild.displayName = "Cat Detector - " + ONOFF

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
                String cmd = "docker ${ONOFF} cat-detector && docker ps"
                host.sshCommand(cmd)
            }
        } catch(Exception err) {
            println("ALERT | Something went wrong")
            println("ERROR | Message: ${err.getMessage()}")
        }
    }
}