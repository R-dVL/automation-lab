package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

def call() {
    node {
        try {
            stage('Pipeline Setup') {
                // Clean before build
                cleanWs()
                sh('git clone https://github.com/R-dVL/automation-lab.git')
            }

            stage('Host Setup'){
                    Host host = new Host(this, HOST)
                    print(host)

                    withCredentials([
                        usernamePassword(credentialsId: 'server-credentials', usernameVariable: 'user', passwordVariable: 'password')]) {
                        host.setUser(user)
                        host.setPassword(password)
                    }

                    withCredentials([
                        string(credentialsId: 'server-ip', variable: 'ip')]) {
                        host.setIp(ip)
                    }
            }

            stage('Execute Command') {
                script {
                    // Remote params
                    def remote = [:]
                    remote.name = host.getName()
                    remote.host = host.getIp()
                    remote.user = host.getUser()
                    remote.password = host.getPassword()
                    remote.port = 22
                    remote.allowAnyHosts = true

                    // Execute command
                    sshCommand remote: remote, command: "systemctl status", sudo: false
                }
            }
        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error("MESSAGE | ${err.getMessage()}")
        }
    }
}