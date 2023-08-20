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
            sh "cat /etc/passwd"
            stage('Deploy') {
                sshagent(credentials: ['server-ssh-key']) {
                    sh """
                       [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 700 ~/.ssh
                       ssh-keyscan -t rsa,dsa ${host.getIp()} >> ~/.ssh/known_hosts
                       scp target/${prj.getArtifactId()}.tar.gz ${host.getUser()}@${host.getIp()}:${prj.getDestination()}
                    """
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}