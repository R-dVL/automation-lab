package com.rdvl.jenkinsLibrary

import java.time.LocalDate

def call() {
    node ('docker-agent') {
        // Environment variables
        environment {
            configuration
            host
        }
        // Pipeline error control
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson
            // Default Params
            host = new Host(this, HOST)

            def date
            if (env.DATE.isEmpty()) {
                date = LocalDate.now()
            } else {
                date = env.DATE
            }

            currentBuild.displayName = "Cat-Watcher Dataset Update"

            // Stages
            // TODO: Retrieve host credentials in Host constructor
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

            stage('Download Photos') {
                script {
                    def cats = []
                    def not_cats = []

                    sh(script: "curl http://192.168.1.55:3001/photos/date/${date} --output photos.json", returnStdout: true).trim()
                    def photos = readJSON file: 'photos.json'

                    photos.each { photo ->
                        if(photo.cat) {
                            cats.add(photo.image)
                        } else {
                            not_cats.add(photo.image)
                        }
                    }

                    dir('cats') {
                        def count = 0
                        for(image in cats) {
                            if(image != null) {
                                def file = "${date}_cat_${count}.jpg"
                                writeFile file: file, text: image, encoding: 'Base64'
                                host.sshPut(file, '/home/jenkins/cat-watcher/dataset/cats')
                                count += 1
                                print("CATS | ${count} of ${cats.size()}")
                            }
                        }
                        print("Cat files written: ${count}")
                        currentBuild.description = "Cats: ${count} | "
                    }

                    dir('not_cats') {
                        def count = 0
                        for(image in not_cats) {
                            if(image != null) {
                                def file = "${date}_not_cat_${count}.jpg"
                                writeFile file: file, text: image, encoding: 'Base64'
                                host.sshPut(file, '/home/jenkins/cat-watcher/dataset/not_cats')
                                count += 1
                                print("NOT CATS | ${count} of ${not_cats.size()}")
                            }
                        }
                        print("Not cat files written: ${count}")
                        currentBuild.description = currentBuild.description + "Not cats: ${count}"
                    }
                }
            }

        } catch(Exception e) {
            println("ALERT | Something went wrong")
            error(e.getMessage())
        }
    }
}