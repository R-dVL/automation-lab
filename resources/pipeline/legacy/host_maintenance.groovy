package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for backing up data using Ansible playbooks on a remote Docker agent.
 *
 * This pipeline sets up the environment, performs connectivity tests on a remote host,
 * downloads Ansible playbooks from a Git repository, and executes a backup playbook.
 */
def call(host_name) {
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

                    // Clone Ansible Playbooks repository
                    checkout scmGit(
                        branches: [[name: "master"]],
                        userRemoteConfigs: [[url: "https://github.com/r-dvl/ansible-playbooks.git"]]
                    )
                }

                stage('Connectivity Test') {
                    // Host SSH accesible check
                    def sshResult = host.sshCommand('whoami')
                    if (sshResult != 'jenkins') {
                        error("SSH Connection failed: ${sshResult}")
                    } else {
                        utils.log(text='Host accesible', color='green')
                    }
                }

                stage('Host Maintenance') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/host-maintenance.yaml",
                        credentialsId: "${host.getCredentialsId()}",
                        colorized: true,
                        extras: "-e host=${host.getName()} -v"
                    )
                }

            } catch(Exception e) {
                buildError = e.getMessage()
                error(buildError)

            } finally {
                switch(currentBuild.currentResult) {
                    case 'SUCCESS':
                        String title = "${JOB_NAME} - SUCCESS"
                        String message = "Maintenance Done"
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