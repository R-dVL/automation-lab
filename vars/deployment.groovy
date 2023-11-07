package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
                project
            }
            try {
                stage('Setup') {
                    cleanWs()
                    // Configuration
                    String configurationJson = libraryResource resource: 'configuration.json'
                    configuration = readJSON text: configurationJson

                    // Project to deploy
                    project = new Project(this, NAME, VERSION)
                    project.init()

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

                    // Remove any old fingerprints for the host
                    sh(script: "ssh-keygen -R 192.168.1.55")

                    // Add SSH fingerprints for the host
                    sh(script: "ssh-keyscan -t ecdsa,ed25519 -H 192.168.1.55 >> ~/.ssh/known_hosts 2>&1")

                    // Host accesible check
                    try {
                        sshagent(credentials: ['jenkins']) {
                            def sshResult = sh(script: "whoami", returnStdout: true).trim()
                            println("SSH connection with user: ${sshResult} OK")
                        }
                    } catch (e) {
                        println("SSH connection error: ${e}")
                    }
                }

                stage('Prepare') {
                    // TODO: Tests and Sonar
                }

                stage('Deploy') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/deploy.yaml",
                        credentialsId: 'jenkins',
                        hostKeyChecking: false,
                        colorized: true,
                        extras: "-e ${project} -v")
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}