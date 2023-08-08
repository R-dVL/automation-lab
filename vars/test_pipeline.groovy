package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Test'){
                def config = readJSON file: 'resources/static_configuration.json'
                println config
            }
        } catch (Exception e) {
            println("ALERT | ${e.getMessage()}")
            println("ERROR | ${e}")
            error("Build Failure")
        }
    }
}