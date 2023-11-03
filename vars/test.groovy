package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            def creds = utils.retrieveCredentials()
            print creds
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}