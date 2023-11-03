package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            def creds = utils.retrieveCredentials()
            echo creds
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}