import java.time.LocalDate;

def call() {
    node {
        String fileName
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
                    host.sshCommand(CMD)
                }
            }

            stage('Create Backup') {
                // Get actual date and time
                script {
                    // Execute command
                    sshCommand(
                        remote: remote,
                        command: "tar -czvf /DATA/Backups/Gallery/${fileName}.tar.gz /DATA/Gallery")
                }
            }

            stage('Delete Old Backups') {
                script {
                    // Execute command
                    sshCommand(
                        remote: remote,
                        command: "find /DATA/Backups/Gallery/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +")
                }
                currentBuild.description = "${HOST_NAME}: Success"
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error("MESSAGE | ${err.getMessage()}")
        }
    }
}