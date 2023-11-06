package com.rdvl.jenkinsLibrary

import java.time.LocalDate

def call() {
    node ('docker-agent') {
        environment {
            configuration
            host
        }
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Host setup
            host = new Host(this, 'server')
            host.init()

            // Define file name
            LocalDate date = LocalDate.now();
            String fileName = "gallery_backup_" + date.toString().replace('-', '_')

            stage('Create Backup') {
                host.sshCommand("tar -czvf /DATA/Backups/Gallery/${fileName}.tar.gz /DATA/Gallery")
            }

            stage('Delete Old Backups') {
                host.sshCommand("find /DATA/Backups/Gallery/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +")
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}