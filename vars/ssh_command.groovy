package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for executing a command on a remote Docker agent and performing connectivity tests.
 *
 * This pipeline sets up the environment, performs connectivity tests on a remote host,
 * and executes a specified command remotely on a Docker agent.
 */
def call(cmd, sudo, host_name) {
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
                    host = new Host(this, host_name)
                    host.init()

                    currentBuild.displayName = "${host_name}"    // Build name
                    currentBuild.description = "${cmd}"    // Build description
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
                    def cmdResult = host.sshCommand(cmd, sudo)
                    print("Result: ${cmdResult}")
                }

            } catch(Exception e) {
                sh(script: """curl -X POST -H "Content-Type: application/json" -d '{"build_name":"${JOB_NAME}", "build_result":"FAILED", "build_error":"${e.getMessage()}"}' http://192.168.1.55:8123/api/webhook/jenkins""")
                error(e.getMessage())
            }
        }
    }
}