/**
 * Singleton class providing utility methods for Jenkins pipelines.
 */

/**
* Retrieves username and password from Jenkins credentials.
*
* @param credentialsId The ID of the Jenkins credentials.
* @return A map containing 'user' and 'password'.
*/
def retrieveCredentials(credentialsId) {
    withCredentials(
        [usernamePassword(credentialsId: credentialsId, usernameVariable: 'user', passwordVariable: 'password')]) {
            return [user: user, password: password]
    }
}

/**
* Logs a message to the console with optional color formatting.
*
* @param text The message to be logged.
* @param color The color to use for formatting (e.g., 'red', 'green', 'blue'). Default is 'none'.
*/
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

def notification(title, message) {
    sh(script: """curl -X POST -H "Content-Type: application/json" -d '{"title":"${title}", "message":"${message}}"}' http://192.168.1.55:8123/api/webhook/jenkins""")
}