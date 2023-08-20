package com.github.rdvl.automationLibrary

def call() {
    agent {
        docker {
            image 'maven:3.9.3-eclipse-temurin-17-alpine'
            args '-v /root/.m2:/root/.m2'
        }
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params

            stage('Test') {
                cleanWs()
                Project prj = new Project(this, NAME, VERSION)
                print(prj)
                prj.downloadCode()
                sh 'mvn -B -DskipTests clean package'
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}