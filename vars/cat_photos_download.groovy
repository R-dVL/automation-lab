package com.rdvl.automationLibrary

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

                    def response = sh(script: "curl http://192.168.1.55:3001/photos/date/${date} --output photos.json", returnStdout: true).trim()
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
                                writeFile file:"${date}_cat_${count}.jpg", text: image.toString().decodeBase64()
                                count += 1
                            }
                        }
                        print("Cat files written: ${count}")
                    }
/*
                    dir('not_cats') {
                        def count = 0
                        for(image in cats) {
                            def imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(image)
                            writeFile file: "not_cat_${count}_${date}.jpg", binary: imageBytes, encoding: 'ISO-8859-1'
                            count += 1
                        }
                        print("Not cat files written: ${count}")
                    }
*/
                }
            }

        } catch(Exception e) {
            println("ALERT | Something went wrong")
            error(e.getMessage())
            //error(e.getMessage())
        }
    }
}