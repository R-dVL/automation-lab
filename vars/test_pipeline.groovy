package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Pipeline Setup') {
                // Clean before build
                cleanWs()
                sh('git clone https://github.com/R-dVL/automation-lab.git')
            }

            stage('Host Setup'){
                Host host = new Host(this, HOST)
                script {
                    // Retrieve info from Jenkins
                    // User & Password
                    pipeline.withCredentials([
                        usernamePassword(credentialsId: "${host.getConfigCredentials()}", usernameVariable: 'user', passwordVariable: 'password')]) {
                            host.setUser(user)
                            host.setPassword(password)
                    }

                    // IP
                    pipeline.withCredentials([
                        string(credentialsId: "${host.getConfigIp()}", variable: 'ip')]) {
                            host.setIp(ip)
                    }

                    print(host)
                    host.sshCommand('status')
                }
            }
        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error("MESSAGE | ${err.getMessage()}")
        }
    }
}