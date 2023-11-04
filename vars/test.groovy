package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            sshagent(credentials: ['jenkins']) {
            sh '''
                [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                ssh-keyscan -t rsa,dsa 192.168.1.55 >> ~/.ssh/known_hosts
                ssh jenkins@192.168.1.55 ...
            '''
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}