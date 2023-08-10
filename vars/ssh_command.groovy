package io.rdvl.automationLibrary

def call() {

    def constants = Constants.getInstance()

    node {
        try {
            stage('Pipeline Setup') {
                // Clean before build
                cleanWs()
                // Clone Repo
                sh("git clone ${constants.repoURL}")
            }

            // Default Params
            Host host = new Host(this, HOST)

            currentBuild.displayName = "SSH Command - " + HOST
            currentBuild.description = CMD

            stage('Host Setup') {
                script {
                    // Retrieve info from Jenkins
                    // User & Password
                    withCredentials([
                        usernamePassword(credentialsId: host.getConfigCredentials(), usernameVariable: 'user', passwordVariable: 'password')]) {
                            host.setUser(user)
                            host.setPassword(password)
                    }

                    // IP
                    withCredentials([
                        string(credentialsId: host.getConfigIp(), variable: 'ip')]) {
                            host.setIp(ip)
                    }
                }
            }

            stage('Execute Command'){
                result = host.sshCommand(CMD)
            }
        } catch(Exception err) {
            println("ALERT | Something went wrong")
            println("ERROR | Message: ${err.getMessage()}")
        }
    }
}