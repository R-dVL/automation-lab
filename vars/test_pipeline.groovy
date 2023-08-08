package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Test'){
                def configurationText = new URL ('https://github.com/R-dVL/automation-lab/blob/fc4e611929787b42079b3bd29d1c03025760b565/resources/static_configuration.json').getText()
                def configuration = readJSON text: configuration
                println(configuration)
            }
        } catch (Exception e) {
            println("ALERT | ${e.getMessage()}")
            println("ERROR | ${e}")
            error("Build Failure")
        }
    }
}