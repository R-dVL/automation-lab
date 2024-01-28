package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for backing up data using Ansible playbooks on a remote Docker agent.
 *
 * This pipeline sets up the environment, performs connectivity tests on a remote host,
 * downloads Ansible playbooks from a Git repository, and executes a backup playbook.
 */
def call(folder_name, host_name) {
    node () {
        ansiColor('xterm') {
            environment {
                configuration
                host
                src_path
                dest_path
                buildError
            }
            try {
                stage('Prepare') {
                    cleanWs()    //Clean Workspace
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))    // Read configuration file

                    // Create Host object
                    host = new Host(this, host_name)
                    host.init()

                    // Retrieve paths
                    src_path = configuration.automation.backups."${folder_name}".src_path
                    dest_path = configuration.automation.backups."${folder_name}".dest_path

                    currentBuild.displayName = "${src_path}"    // Build name
                    currentBuild.description = "${dest_path}"    // Build description

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

                stage('Sync Folder') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/sync-folder.yaml",
                        credentialsId: "${host.getCredentialsId()}",
                        colorized: true,
                        extras: "-e src_path=${src_path} -e dest_path=${dest_path} -vv"
                    )
                }

            } catch(Exception e) {
                buildError = e.getMessage()
                error(buildError)

            } finally {
                switch(currentBuild.currentResult) {
                    case 'SUCCESS':
                        String title = "${JOB_NAME} - SUCCESS"
                        String message = "Source: ${src_path} - Destination: ${dest_path}"
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