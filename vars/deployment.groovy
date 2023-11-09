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

                stage('Prepare') {
                    // TODO: Tests and Sonar
                }

                stage('Deploy') {
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/deploy.yaml",
                        credentialsId: 'jenkins',
                        colorized: true,
                        extras: "-e ${project} -v")
                }

                stage('Post Implantation') {
                    host.sshGet("./', '/opt/apps/${project.getname()}/${project.getversion()}/${project.getname()}.log")
                    archiveArtifacts artifacts: "./${project.getname()}.log"
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}