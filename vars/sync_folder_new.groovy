package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for backing up data using Ansible playbooks on a remote Docker agent.
 *
 * This pipeline sets up the environment, performs connectivity tests on a remote host,
 * downloads Ansible playbooks from a Git repository, and executes a backup playbook.
 */
def call() {
    node ('docker-agent') {
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

                    folders = FOLDERS.split(', ')
                }

                connectivity_test(host)

                stage('Backup') {
                    def parallelSync = [:]

                    for(folder in folders) {
                        parallelSync = ["${folder}"] = {
                            ansiblePlaybook(
                                inventory:'./inventories/hosts.yaml',
                                playbook: "./playbooks/sync-folder.yaml",
                                credentialsId: "${host.getCredentialsId()}",
                                colorized: true,
                                extras: "-e src_path=${folder} -vv")
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