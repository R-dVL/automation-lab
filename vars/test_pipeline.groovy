package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Test'){
                def configuration = readJSON file: './automation-lab/resources/static_configuration.json'
                println(configuration)
            }
        } catch (Exception e) {
            println("ALERT | ${e.getMessage()}")
            println("ERROR | ${e}")
            error("Build Failure")
        }
    }
}