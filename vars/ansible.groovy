package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            try {
                stage('Download Ansible Playbooks') {
                    git branch: 'master',
                        url: 'https://github.com/R-dVL/ansible-playbooks.git'
                }
                // Configuration instance
                String configurationJson = libraryResource resource: 'configuration.json'
                configuration = readJSON text: configurationJson

                // Project to deploy
                Project prj = new Project(this, 'lima-backend', '1.1.0')

                stage('Execute Playbook') {
                    def credentials = utils.retrieveCredentials('mongo-credentials')
                    ansiblePlaybook(
                        inventory:'./inventories/hosts.yaml',
                        playbook: "./playbooks/deploy-backend.yaml",
                        credentialsId: 'jenkins',
                        colorized: true,
                        extras: "-e ${prj} user=${credentials.user} password=${credentials.password} -v")
                }
            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}