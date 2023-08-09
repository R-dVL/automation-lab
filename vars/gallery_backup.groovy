package io.rdvl.automationLibrary

import java.time.LocalDate;

def call() {
    node {
        try {
            // Default Params
            Host host = new Host(this, 'Server')
            String fileName

            stage('Pipeline Setup') {
                // Clean before build
                cleanWs()
                sh('git clone https://github.com/R-dVL/automation-lab.git')
            }

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

            stage('Create Backup') {
                // Define file name
                LocalDate date = LocalDate.now();
                fileName = "gallery_backup_" + date.toString().replace('-', '_')

                // Command
                host.sshCommand('tar -czvf /DATA/Backups/Gallery/${fileName}.tar.gz /DATA/Gallery')
            }

            stage('Delete Old Backups') {
                host.sshCommand('find /DATA/Backups/Gallery/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +')
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error("MESSAGE | ${err.getMessage()}")
        }
    }
}