package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Test'){
                sh('pwd')
                sh('ls')
                def configuration = readJSON file: 'resources/static_configuration.json'
                println(configuration)
            }
        } catch (Exception e) {
            println("ALERT | ${e.getMessage()}")
            println("ERROR | ${e}")
            error("Build Failure")
        }
    }
}