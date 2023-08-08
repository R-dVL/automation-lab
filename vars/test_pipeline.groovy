package io.rdvl.automationLibrary

def call() {
    node {
        stage('Test'){
                // Clean before build
                cleanWs()
                // We need to explicitly checkout from SCM here
                checkout scm
                echo "Building ${env.JOB_NAME}..."
        }
    }
}