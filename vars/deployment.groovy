package com.rdvl.jenkinsLibrary
/**
 * Jenkins pipeline for deploying a project using Ansible playbooks.
 *
 * This pipeline sets up the environment, performs connectivity tests, prepares the project,
 * deploys the project using Ansible playbooks, and handles post-implementation tasks.
 */
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
                }

                connectivity_test(host)

                stage('Prepare') {
                    // TODO: Tests and Sonar
                    // Donwload Source Code
                    def downloadResult = sh(script: "git clone --depth 1 --branch ${project.getVersion()} ${project.getUrl()}", returnStatus: true)
                }

                stage('Build') {
                    // Build image
                    def buildResult = sh(script:"docker build -t ghcr.io/r-dvl/${project.getName()}:${project.getVersion()} .", returnStatus: true)
                    print(buildResult)

                    // Registry login
                    withCredentials([
                        string(credentialsId: 'github-package-token	', variable: 'token')]) {
                            def loginResult = sh(script: "echo ${token} | docker login ghcr.io -u r-dvl --password-stdin", returnStatus: true)
                            print(loginResult)
                        }

                    // Push
                    def loginsResult = sh(script: "docker push ghcr.io/r-dvl/${project.getName()}:${project.getVersion()}", returnStatus: true)
                }

                stage('Deploy') {
                    def deployResult = host.sshCommand("docker run ghcr.io/r-dvl/${project.getName()}:${project.getVersion()}")
                    print(deployResult)
                }

                stage('Post Implantation') {
                    // TODO: Check status
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}