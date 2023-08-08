package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Test'){
                Host test = new Host(HOST)
                println("Name -> " + test.getName())
                println("Credentials -> " + test.getCredentials())
                println("Config -> " + test.getConfig())
            }
        } catch (Exception e) {
            println("ALERTA | ${e.getMessage()}")
            error(" ERROR | ${e}")
        }
    }
}