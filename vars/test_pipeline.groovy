package io.rdvl.automationLibrary

def call() {
    node {
        stage('Test'){
            Host test = new Host(HOST)
            println("Name -> " + test.getName())
            println("Credentials -> " + test.getCredentials())
            println("Config -> " + test.getConfig())
        }
    }
}