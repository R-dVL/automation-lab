package com.rdvl.jenkinsLibrary


def call() {

    def retrieveCredentials(credentialsId) {
        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'user', passwordVariable: 'password')]) {
            return [user: user, password: password]
        }
    }
}
