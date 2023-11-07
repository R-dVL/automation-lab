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
                        error "Host not reachable: ${result}"
                    }

                    // Host accesible check
                    try {
                        sshagent(credentials: ['jenkins']) {
                            def sshResult = sh(script: "whoami", returnStdout: true).trim()
                            println("SSH connection with user: ${result} OK")
                        }
                    } catch (e) {
                        println("SSH connection error: ${e}")
                    }
                }

                stage('Prepare') {
                    // TODO: Tests and Sonar
                }

                stage('Deploy') {
                    def credentials = utils.retrieveCredentials('mongo-credentials')
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/deploy.yaml",
                        credentialsId: 'jenkins',
                        colorized: true,
                        extras: "-e ${project} -v")
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}