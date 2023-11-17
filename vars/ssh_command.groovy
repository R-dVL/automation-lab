package com.rdvl.jenkinsLibrary


def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
                host
            }
            try {
                stage('Setup') {
                    // Configuration instance
                    String configurationJson = libraryResource resource: 'configuration.json'
                    configuration = readJSON text: configurationJson

                    // Default Params
                    host = new Host(this, HOST)
                    host.init()
                }

                stage('Connectivity Test') {
                    // Host alive check
                    def pingResult = sh(script: "nc -z -w5 ${host.getIp()} 80", returnStatus: true)

                    if (pingResult == 0) {
                        utils.log("Host reachable", 'green')
                    } else {
                        error("Host not reachable: ${pingResult}")
                    }

                    // Host SSH accesible check
                    def sshResult = host.sshCommand('whoami')
                    if (sshResult != 'jenkins') {
                        error("SSH Connection failed: ${sshResult}")
                    } else {
                        utils.log("Host accesible", 'green')
                    }
                }

                stage('Execute Command') {
                    def result = host.sshCommand(CMD, SUDO)
                    print("Result: ${result}")
                }

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}