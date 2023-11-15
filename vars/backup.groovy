package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            try {
                stage('Setup') {
                    cleanWs()
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

                    // Host accesible with ssh key check
                    try {
                        sshagent(credentials: ['jenkins']) {
                            sh(script: """ssh jenkins@192.168.1.55""")
                            println("SSH connection OK")
                        }
                    } catch (e) {
                        error("SSH connection error: ${e}")
                    }
                }

                stage('Backup') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/backup.yaml",
                        credentialsId: 'jenkins',
                        colorized: true,
                        extras: "-vvv")
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}