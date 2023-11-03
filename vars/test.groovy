package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        try {
            utils.myTest()
        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}