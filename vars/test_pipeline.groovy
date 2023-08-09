package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

def call() {
    node {
        environment {
            CONFIGURATION
        }
        try {
            stage('Pipeline Setup') {
                // Clean before build
                cleanWs()
                sh('git clone https://github.com/R-dVL/automation-lab.git')
                def jsonSlurper = new JsonSlurper()
                CONFIGURATION = jsonSlurper.parse(new File('./automation-lab/resources/configuration.json'))
            }

            stage('Host Setup'){
                    Host host = new Host(HOST, CONFIGURATION)
                    print(host)
            }
        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error("MESSAGE | ${err.getMessage()}")
        }
    }
}