//// UTILS SINGLETON ////

def retrieveCredentials(credentialsId) {
    withCredentials(
        [usernamePassword(credentialsId: credentialsId, usernameVariable: 'user', passwordVariable: 'password')]) {
            return [user: user, password: password]
    }
}

String log(text, color = 'none') {
    switch(color) {
        case 'black':
            println("\e[30m${text}\e[0m")
            break

        case 'red':
            println("\e[31m${text}\e[0m")
            break

        case 'green':
            println("\e[32m${text}\e[0m")
            break

        case 'yellow':
            println("\e[33m${text}\e[0m")
            break
        
        case 'blue':
            println("\e[34m${text}\e[0m")
            break
        
        case 'purple':
            println("\e[35m${text}\e[0m")
            break
        
        case 'cyan':
            println("\e[36m${text}\e[0m")
            break

        case 'white':
            println("\e[37m${text}\e[0m")
            break

        case 'none':
            println(text)
            break

        default:
            println(text)
    }
}