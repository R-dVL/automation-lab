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
                stage('Prepare') {
                    cleanWs()    //Clean Workspace
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))    // Read configuration file

                    // Create Host object
                    host = new Host(this, HOST)
                    host.init()

                    currentBuild.displayName = "${HOST}"    // Build name
                    currentBuild.description = "${CMD}"    // Build description
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