package com.github.rdvl.automationLibrary

import java.time.LocalDate

def call() {
    node {
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            LocalDate date = LocalDate.now()

            // Stages
            stage('Download Photos') {
                script {
                    def cats = []
                    def not_cats = []

                    def response = sh(script: "curl http:192.168.1.55:3001/photos/date/${date} --output photos.json", returnStdout: true).trim()
                    def photos = readJSON file: 'photos.json'

                    photos.each { photo ->
                        if(photo.cat.toString() == "True") {
                            cats.add(photo.image)
                        } else {
                            not_cats.add(photo.image)
                        }
                    }
                }
            }

        } catch(Exception e) {
            println("ALERT | Something went wrong")
            error(e.getMessage())
        }
    }
}