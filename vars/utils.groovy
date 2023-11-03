//// UTILS SINGLETON ////

def retrieveCredentials(credentialsId) {
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'user', passwordVariable: 'password')]) {
        return [user: user, password: password]
    }
}
