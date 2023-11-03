//// UTILS SINGLETON ////

@NonCPS
def retrieveCredentials(credentialsId) {
    script {
        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'user', passwordVariable: 'password')]) {
            return [user: user, password: password]
        }
    }
}
