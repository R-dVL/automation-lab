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
            text = "\e[30m${text}\e[0m"
            break

        case 'red':
            text = "\e[31m${text}\e[0m"
            break

        case 'green':
            text = "\e[32m${text}\e[0m"
            break

        case 'yellow':
            text = "\e[33m${text}\e[0m"
            break
        
        case 'blue':
            text = "\e[34m${text}\e[0m"
            break
        
        case 'purple':
            text = "\e[35m${text}\e[0m"
            break
        
        case 'cyan':
            text = "\e[36m${text}\e[0m"
            break

        case 'white':
            text = "\e[37m${text}\e[0m"
            break

        case 'none':
            break

        default:
            println('Unknown color')
    }
    println(text)
}