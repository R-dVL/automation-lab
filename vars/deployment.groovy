package com.github.rdvl.automationLibrary

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

            stage('Prepare') {
                cleanWs()
                Project prj = new Project(this, NAME, VERSION)
                print(prj)

                prj.prepare()
            }

            stage('Deploy') {

            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}