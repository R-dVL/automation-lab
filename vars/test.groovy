package com.rdvl.jenkinsLibrary

def call() {
    node () {
        try {
            stage('Test') {
                def tokenVal
                withCredentials([
                    string(credentialsId: 'github-package-token	', variable: 'token')]) {
                        script {
                            tokenVal = token
                        }
                    }
                print(tokenVal)
            }
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}