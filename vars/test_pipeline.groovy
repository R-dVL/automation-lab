package io.rdvl.automationLibrary

def call() {
    node {
        environment {
            CONFIG
        }

        Configuration cfg = new Configuration(env.WORKSPACE)
        CONFIG = cfg.getConfiguration()

        try {
            stage('Test'){
                    // Clean before build
                    cleanWs()
                    sh('git clone https://github.com/R-dVL/automation-lab.git')
                    Host host = new Host(HOST, CONFIG)
                    print(host)
            }
        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error("MESSAGE | ${err.getMessage()}")
        }
    }
}