package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
            mongo_user
            mongo_password
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            Project prj = new Project(this, NAME, VERSION)
            host = new Host(this, 'server')

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
                    // IP
                    withCredentials([string(credentialsId: host.getConfigIp(), variable: 'ip')]) {
                        host.setIp(ip)
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
            println("ALERT | Something went wrong")
            error(e.getMessage())
        }
    }
}