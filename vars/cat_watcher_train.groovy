package com.rdvl.automationLibrary

import java.time.LocalDate

def call() {
    node {
        // Environment variables
        environment {
            cfg
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            host = new Host(this, HOST)

            // Stages
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

            stage('Prepare') {
                git branch: 'ai_model', url: 'https://github.com/R-dVL/cat-watcher.git'
                sh('cd ./cat-watcher/resources')
                host.sshGet('/home/jenkins/cat-watcher/dataset')
            }

        } catch(Exception e) {
            println("ALERT | Something went wrong")
            error(e.getMessage())
        }
    }
}