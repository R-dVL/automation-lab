def call() {
    node {
        environment{
            HOST_NAME
            HOST_IP
            HOST_USER
            HOST_PASSWORD
        }

        try {
            stage('Configure Host') {
                // Configure env vars in function of the host selected
                switch(HOST) {
                    case "Server":
                        HOST_NAME = 'rdvl-server'

                        withCredentials([
                            usernamePassword(
                                credentialsId: 'server-credentials',
                                usernameVariable: 'user',
                                passwordVariable: 'password')
                        ]) {
                            HOST_USER = user
                            HOST_PASSWORD = password
                        }

                        withCredentials([
                            string(
                                credentialsId: 'server-ip',
                                variable: 'ip',)
                        ]) {
                            HOST_IP = ip
                        }
                        break

                    case "RPi":
                        HOST_NAME = 'rastberry'

                        withCredentials([
                            usernamePassword(
                                credentialsId: 'rpi-credentials',
                                usernameVariable: 'user',
                                passwordVariable: 'password')
                        ]) {
                            HOST_USER = user
                            HOST_PASSWORD = password
                        }

                        withCredentials([
                            string(
                                credentialsId: 'rpi-ip',
                                variable: 'ip',)
                        ]) {
                            HOST_IP = ip
                        }
                        break

                    default:
                        error("Error, selected HOST is not configured")
                        break
                }
                // Change build name
                currentBuild.displayName = "${HOST_NAME}: ${SERVICE} - ${COMMAND}"
            }

            stage('Execute Command') {
                script {
                    // Remote params
                    def remote = [:]
                    remote.name = HOST_NAME
                    remote.host = HOST_IP
                    remote.user = HOST_USER
                    remote.password = HOST_PASSWORD
                    remote.port = 22
                    remote.allowAnyHosts = true

                    // Execute command
                    sshCommand remote: remote, command: "systemctl ${COMMAND} ${SERVICE}", sudo: false
                    currentBuild.description = "${HOST_NAME}: Success"
                }
            }

        } catch (Exception e) {
            // Build failed
            currentBuild.description = "${HOST_NAME}: Failed executing command -> ${e}"
            currentBuild.result = 'FAILURE'
            println("ALERT | ${e.getMessage()}")
            println("ERROR | ${e}")
            error("Build Failure")
        }
    }
}