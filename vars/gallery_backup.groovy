package com.rdvl.automationLibrary

import java.time.LocalDate

def call() {
    node {
        // Environment variables
        environment {
            cfg
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            host = new Host(this, HOST)
            // Define file name
            LocalDate date = LocalDate.now();
            String fileName = "gallery_backup_" + date.toString().replace('-', '_')

            stage('Host Setup') {
                // Retrieve info from Jenkins
                script {
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
                host.sshCommand("tar -czvf /DATA/Backups/Gallery/${fileName}.tar.gz /DATA/Gallery")
            }

            stage('Delete Old Backups') {
                host.sshCommand("find /DATA/Backups/Gallery/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +")
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}