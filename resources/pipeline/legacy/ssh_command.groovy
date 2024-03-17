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
                buildError
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
                        utils.log(text = 'Host accesible', color = 'green')
                    }
                }

                stage('Execute Command') {
                    def cmdResult = host.sshCommand(cmd, sudo)
                    print("Result: ${cmdResult}")
                }

            } catch(Exception e) {
                buildError = e.getMessage()
                error(buildError)

            } finally {
                switch(currentBuild.currentResult) {
                    case 'SUCCESS':
                        String title = "${JOB_NAME} - SUCCESS"
                        String message = "Command: ${cmd}"
                        utils.notification(title, message)
                        break
                    
                    case 'FAILURE':
                        String title = "${JOB_NAME} - FAILED"
                        String message = "${buildError}"
                        utils.notification(title, message)
                        break
                    
                    case 'UNSTABLE':
                        String title = "${JOB_NAME} - UNSTABLE"
                        String message = "Build unstable"
                        utils.notification(title, message)
                        break

                    default:
                        String title = "${JOB_NAME}"
                        String message = "Unknown result"
                        utils.notification(title, message)
                        break
                }
            }
        }
    }
}