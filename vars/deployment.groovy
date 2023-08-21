package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
            github_user
            github_token
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            Project prj = new Project(this, NAME, VERSION)
            host = new Host(this, 'server')

            stage('Prepare') {
                cleanWs()
                script {
                    // User & Password
                    withCredentials([usernamePassword(credentialsId: 'github-package-token', usernameVariable: 'user', passwordVariable: 'token')]) {
                        github_user = user
                        github_token = token
                    }
                }
                prj.getDeploymentTech().prepare()
            }

            stage('Host Setup') {
                script {
                    // User & Password
                    withCredentials([usernamePassword(credentialsId: host.getConfigCredentials(), usernameVariable: 'user', passwordVariable: 'password')]) {
                        host.setUser(user)
                        host.setPassword(password)
                    }
                    // IP
                    withCredentials([string(credentialsId: host.getConfigIp(), variable: 'ip')]) {
                        host.setIp(ip)
                    }
                }
            }

            stage('Deploy') {
                prj.getDeploymentTech().deploy()
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}