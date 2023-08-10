package io.rdvl.automationLibrary

import java.time.LocalDate;
import groovy.json.JsonSlurperClassic


def call() {
    node {
        environment {
            constants
            configuration
        }
        try {
            stage('Pipeline Setup') {
                // Constants instance
                constants = Constants.getInstance()
                // Clean before build
                cleanWs()
                // Clone Repo
                sh("git clone ${constants.repoURL}")

                // Retrieve Configuration
                def jsonSlurperClassic = new JsonSlurperClassic()
                configuration = jsonSlurperClassic.parse(new File("${WORKSPACE}${constants.configPath}"))
            }

            // Default Params
            Host host = new Host(this, 'Server')

            LocalDate date = LocalDate.now();
            String fileName = "gallery_backup_" + date.toString().replace('-', '_')

            currentBuild.displayName = "Gallery Backup"

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
                host.sshCommand('tar -czvf /DATA/Backups/Gallery/${fileName}.tar.gz /DATA/Gallery')
            }

            stage('Delete Old Backups') {
                host.sshCommand('find /DATA/Backups/Gallery/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +')
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            println("ERROR | Message: ${err.getMessage()}")
        }
    }
}