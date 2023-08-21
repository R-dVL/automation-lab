package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            Project prj = new Project(this, NAME, VERSION)
            Host host = new Host(this, 'server')

            stage('Prepare') {
                cleanWs()
                prj.prepare()
            }

            stage('Host Setup') {
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

            stage('Deploy') {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: '192.168.1.55:8081',
                    groupId: 'com.github.rdvl.catWatcher',
                    version: 'v1.0.0',
                    repository: 'cat-watcher',
                    credentialsId: 'nexus-credentials',
                    artifacts: [
                        [artifactId: 'cat-watcher',
                        classifier: '',
                        file: 'target/' + 'cat-watcher' + '-v1.0.0' + '.jar',
                        type: 'jar']
                    ]
                );
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}