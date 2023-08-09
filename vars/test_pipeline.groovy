package io.rdvl.automationLibrary

def call() {
    node {
        try {
            stage('Test'){
                    // Clean before build
                    cleanWs()
                    sh('git clone https://github.com/R-dVL/automation-lab.git')
                    Host host = new Host(HOST)
                    print(host)
            }
        } catch(Exception err) {
            println(" ALERT | Something went wrong")
            println("MESSAGE| ${err.getMessage()}")
            error(err)
        }
    }
}