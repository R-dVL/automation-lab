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
                log
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

                connectivity_test(host)

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