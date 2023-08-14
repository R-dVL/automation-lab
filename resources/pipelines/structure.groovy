package com.github.rdvl.automationLibrary

import groovy.json.JsonSlurperClassic

def call() {
    node {
        // Environment variables
        environment {
            constants
            configuration
        }
        // Pipeline error control
        try {
            // Constants instance
            constants = Constants.getInstance()
            // Clean before build
            cleanWs()
            // Clone Repo
            git(
                url: "${constants.repoURL}",
                credentialsId: 'github-token',
                branch: 'master'
            )
            // Retrieve Configuration
            def jsonSlurperClassic = new JsonSlurperClassic()
            configuration = jsonSlurperClassic.parse(new File("${WORKSPACE}${constants.configPath}"))

            stage('Stage 1') {

            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            println("ERROR | Message: ${err.getMessage()}")
            error(err)
        }
    }
}