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
                script {
                    def sshPublisher = sshPublisher(
                            publishers: [
                                    sshPublisherDesc(
                                            configName: 'Server',
                                            transfers: [
                                                    sshTransfer(
                                                            sourceFiles: 'target/cat-watcher_v1.0.0.tar.gz',
                                                            removePrefix: 'target/',
                                                            remoteDirectory: '/home/rdvl/'
                                                    )
                                            ]
                                    )
                            ]
                    )
                    sshPublisher.perform(build, launcher, listener)
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}