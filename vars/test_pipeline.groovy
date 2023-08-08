package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Test'){
                def configurationText = new URL ('https://github.com/R-dVL/automation-lab/blob/master/resources/static_configuration.json').getText()
                println(configurationText)
                def configuration = readJSON text: configurationText
                println(configuration)
            }
        } catch (Exception e) {
            println("ALERT | ${e.getMessage()}")
            println("ERROR | ${e}")
            error("Build Failure")
        }
    }
}