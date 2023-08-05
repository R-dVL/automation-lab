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
                switch(HOST) {
                    case "Server":
                        HOST_NAME = 'rdvl-server'

                        withCredentials([
                            usernamePassword(credentialsId: 'server-credentials',
                                usernameVariable = HOST_USER,
                                passwordVariable = HOST_PASSWORD)
                        ])

                        withCredentials([
                            string(credentialsId: 'server-ip',
                                variable = HOST_IP,)
                        ])
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
            currentBuild.description = "${HOST_NAME}: Failed"
            currentBuild.result = 'FAILURE'
            error ("Failed executing command -> ${err}")
        }
    }
}