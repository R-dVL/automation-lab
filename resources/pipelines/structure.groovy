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

            stage('Stage 1') {

            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            println("ERROR | Message: ${err.getMessage()}")
            error(err)
        }
    }
}