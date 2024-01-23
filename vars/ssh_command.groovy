package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for executing a command on a remote Docker agent and performing connectivity tests.
 *
 * This pipeline sets up the environment, performs connectivity tests on a remote host,
 * and executes a specified command remotely on a Docker agent.
 */
def call() {
    node () {
        ansiColor('xterm') {
            environment {
                configuration
                host
            }
            try {
                stage('Setup') {
                    // Read configuration file
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))

                    // Default Params
                    host = new Host(this, HOST)
                    host.init()
                }

                stage('Connectivity Test') {
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