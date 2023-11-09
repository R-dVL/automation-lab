package com.rdvl.jenkinsLibrary

import java.time.LocalDate

def call() {
    node ('docker-agent') {
        environment {
            configuration
            host
        }
        try {
            stage('Setup') {
                // Configuration instance
                String configurationJson = libraryResource resource: 'configuration.json'
                configuration = readJSON text: configurationJson

                // Default Params
                host = new Host(this, 'server')
                host.init()
            }

            // Set facts
            LocalDate date = LocalDate.now();
            String fileName = "gallery_backup_" + date.toString().replace('-', '_')
            String source = '/DATA/Gallery'
            String destination = '/media/devmon/WD_BLACK/NAS/system/backups/gallery'

            stage('Create Backup') {
                host.sshCommand(command: "tar -czvf ${destination}/${fileName}.tar.gz ${source}", sudo: true)
            }

            stage('Delete Old Backups') {
                host.sshCommand(command: "find ${destination}/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +", sudo: true)
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}