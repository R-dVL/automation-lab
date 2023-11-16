package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
                host
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
                }

                stage('Connectivity Check') {
                    // Host alive check
                    // TODO: Dynamic host
                    def pingResult = sh(script: "nc -z -w5 192.168.1.55 80", returnStatus: true)

                    if (pingResult == 0) {
                        println("Host reachable")
                    } else {
                        error("Host not reachable: ${pingResult}")
                    }

                    def sshResult = host.sshCommand('whoami')

                    if (sshResult != 'jenkins') {
                        error("SSH Connection failed: ${sshResult}")
                    } else {
                        println("Host accesible")
                    }
                }

                stage('Backup') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/backup.yaml",
                        credentialsId: 'server-credentials',
                        colorized: true,
                        extras: "-e src_path=${SRC_PATH} -vv")
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}