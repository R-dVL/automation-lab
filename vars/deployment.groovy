package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
            JDK_20_HOME = "/usr/lib/jvm/jdk-20"
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

                def mvnHome = tool name: 'Maven 3.9.4', type: 'maven'
                def mvnCmd = "${mvnHome}/bin/mvn"

                env.PATH = "${env.JDK_20_HOME}/bin:${env.PATH}"

                sh "${mvnCmd} clean install"
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}