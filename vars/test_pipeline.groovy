package io.rdvl.automationLibrary

def call() {
    node {
        stage('Test'){
                // Clean before build
                cleanWs()
                sh('git clone https://github.com/R-dVL/automation-lab.git')
                Configuration config = new Configuration()
                //Host host = new Host(HOST)
                print(config)
        }
    }
}