//// UTILS SINGLETON ////

def retrieveCredentials() {
    withCredentials(
        [usernamePassword(credentialsId: 'server-credentials', usernameVariable: 'user', passwordVariable: 'password')]) {
            return [user: user, password: password]
    }
}

def myTest() {
    echo 'Funsiono'
}
