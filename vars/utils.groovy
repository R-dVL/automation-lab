//// UTILS SINGLETON ////

def retrieveCredentials(credentialsId) {
    withCredentials(
        [usernamePassword(credentialsId: credentialsId, usernameVariable: 'user', passwordVariable: 'password')]) {
            return [user: user, password: password]
    }
}

def log(text, color = 'none') {
    switch(color) {
        case 'black':
            text = "\033[30m${text}\033[0m"
            break

        case 'red':
            text = "\033[31m${text}\033[0m"
            break

        case 'green':
            text = "\033[32m${text}\033[0m"
            break

        case 'yellow':
            text = "\033[33m${text}\033[0m"
            break

        case 'blue':
            text = "\033[34m${text}\033[0m"
            break

        case 'purple':
            text = "\033[35m${text}\033[0m"
            break

        case 'cyan':
            text = "\033[36m${text}\033[0m"
            break

        case 'white':
            text = "\033[37m${text}\033[0m"
            break

        case 'none':
            break

        default:
            println('Unknown color')
    }
    printf(text)
}