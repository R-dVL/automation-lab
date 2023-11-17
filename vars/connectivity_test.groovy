package com.rdvl.jenkinsLibrary

//// COMMON STAGE ////
// Performs a connectivity test

def call(host) {
    try {
        stage('Connectivity Test') {
            // Host alive check
            def pingResult = sh(script: "nc -z -w5 ${host.getIp()} 80", returnStatus: true)

            if (pingResult == 0) {
                utils.log("Host reachable", 'green')
            } else {
                error("Host not reachable: ${pingResult}")
            }

            // Host SSH accesible check
            def sshResult = host.sshCommand('whoami')
            if (sshResult != 'jenkins') {
                error("SSH Connection failed: ${sshResult}")
            } else {
                utils.log("Host accesible", 'green')
            }
        }
    } catch(Exception err) {
        error(err.getMessage())
    }
}