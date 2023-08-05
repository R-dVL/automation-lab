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
                String credentialsSecret
                String ipSecret

                switch(HOST) {
                    case "Server":
                        HOST_NAME = 'rdvl-server'
                        credentialsSecret = 'server-credentials'
                        ipSecret = 'server-ip'
                        break

                    case "RPi":
                        HOST_NAME = 'rastberry'
                        credentialsSecret = 'rpi-credentials'
                        ipSecret = 'rpi-ip'
                        break

                    default:
                        error("Error, selected HOST is not configured")
                        break

                    withCredentials([
                        usernamePassword(
                            credentialsId: '${credentialsSecret}',
                            usernameVariable: 'user',
                            passwordVariable: 'password')
                    ]) {
                        HOST_USER = user
                        HOST_PASSWORD = password
                    }

                    withCredentials([
                        string(
                            credentialsId: '${ipSecret}',
                            variable: 'ip',)
                    ]) {
                        HOST_IP = ip
                    }
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

        } catch (Exception err) {
            // Build failed
            currentBuild.description = "${HOST_NAME}: Failed executing command -> ${err}"
            currentBuild.result = 'FAILURE'
            error ("Failed executing command -> ${err}")
        }
    }
}