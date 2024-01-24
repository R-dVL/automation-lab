package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for backing up data using Ansible playbooks on a remote Docker agent.
 *
 * This pipeline sets up the environment, performs connectivity tests on a remote host,
 * downloads Ansible playbooks from a Git repository, and executes a backup playbook.
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

                    currentBuild.displayName = "${SRC_PATH}"    // Build name
                    currentBuild.description = "${DEST_PATH}"    // Build description

                    // Clone ansible playbooks repository
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
                        utils.log("Host accesible", 'green')
                    }
                }

                stage('Sync Folder') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/sync-folder.yaml",
                        credentialsId: "${host.getCredentialsId()}",
                        colorized: true,
                        extras: "-e src_path=${SRC_PATH} -e dest_path=${DEST_PATH} -vv"
                    )
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}