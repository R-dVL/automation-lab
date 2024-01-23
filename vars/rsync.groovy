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
                stage('Setup') {
                    cleanWs()

                    // Read configuration file
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))

                    // Host init
                    host = new Host(this, 'server')
                    host.init()

                    currentBuild.description = "${FOLDER}"

                    // Donwload Ansible Playbooks
                    git branch: 'master',
                        url: 'https://github.com/r-dvl/ansible-playbooks.git'
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

                stage('Rsync') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/sync-folder.yaml",
                        credentialsId: "${host.getCredentialsId()}",
                        colorized: true,
                        extras: "-e src_path=${FOLDER} -vv"
                    )
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}