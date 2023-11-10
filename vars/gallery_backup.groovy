package com.rdvl.jenkinsLibrary

import java.time.LocalDate

def call() {
    node ('docker-agent') {
        environment {
            configuration
            host
            fileName
            source
            destination
        }
        try {
            stage('Setup') {
                // Configuration instance
                String configurationJson = libraryResource resource: 'configuration.json'
                configuration = readJSON text: configurationJson

                // Default Params
                host = new Host(this, 'server')
                host.init()

                // Set facts
                LocalDate date = LocalDate.now();
                fileName = "gallery_backup_" + date.toString().replace('-', '_')
                source = '/DATA/Gallery'
                destination = '/media/devmon/WD_BLACK/NAS/system/backups/gallery'
            }

            stage('Create Backup') {
                host.sshCommand("tar -czvf ${destination}/${fileName}.tar.gz ${source}", true)
            }

            stage('Delete Old Backups') {
                host.sshCommand("find ${destination}/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +", true)
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}