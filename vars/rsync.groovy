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
                folders
            }
            try {
                stage('Setup') {
                    cleanWs()
                    // Configuration
                    String configurationJson = libraryResource resource: 'configuration.json'
                    configuration = readJSON text: configurationJson

                    // Host init
                    host = new Host(this, 'server')
                    host.init()

                    // Donwload Ansible Playbooks
                    git branch: 'master',
                        url: 'https://github.com/R-dVL/ansible-playbooks.git'

                    // Init folders to synchronize
                    folders = FOLDERS.contains(',') ? FOLDERS.split(', ') : [FOLDERS]
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

                stage('Backup') {
                    def parallelSync = [:]

                    for(folder in folders) {
                        String syncPath = folder
                        parallelSync["${folder}"] = {
                            ansiblePlaybook(
                                inventory:'./inventories/hosts.yaml',
                                playbook: "./playbooks/sync-folder.yaml",
                                credentialsId: "${host.getCredentialsId()}",
                                colorized: true,
                                extras: "-e src_path=${syncPath} -vv")
                        }
                    }
                    parallel parallelSync
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}