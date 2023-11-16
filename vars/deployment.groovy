package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            environment {
                configuration
                project
                host
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

                    host = new Host(this, 'server')
                    host.init()

                    // Donwload Ansible Playbooks
                    git branch: 'master',
                        url: 'https://github.com/R-dVL/ansible-playbooks.git'
                }

                stage('Connectivity Test') {
                    // Host alive check
                    def pingResult = sh(script: "nc -z -w5 ${host.getIp()} 80", returnStatus: true)

                    if (pingResult == 0) {
                        println("Host reachable")
                    } else {
                        error("Host not reachable: ${pingResult}")
                    }

                    // Host SSH accesible check
                    def sshResult = host.sshCommand('whoami')
                    if (sshResult != 'jenkins') {
                        error("SSH Connection failed: ${sshResult}")
                    } else {
                        println("Host accesible")
                    }
                }

                stage('Prepare') {
                    // TODO: Tests and Sonar
                }

                stage('Deploy') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/deploy.yaml",
                        credentialsId: "${host.getCredentialsId()}",
                        colorized: true,
                        extras: "-e ${project} -v")
                }

                stage('Post Implantation') {
                    host.sshGet("./", "/opt/apps/${project.getName()}/${project.getVersion()}/${project.getName()}.log")
                    archiveArtifacts artifacts: "${project.getName()}.log"
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}