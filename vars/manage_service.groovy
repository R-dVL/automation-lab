def call() {
    node {
        stage('Check Service Status') {
            script {
                try{
                    // Check mandatory params
                    try {
                        assert HOST_USER == "" : "HOST_NAME is a Mandatory parameter"
                    } catch (AssertionError err) {
                        throw err
                    }

                    // Change build name
                    currentBuild.displayName = "${HOST_NAME}: ${SERVICE} - ${COMMAND}"

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

                } catch (Exception err) {
                    // Build failed
                    currentBuild.description = "${HOST_NAME}: Failed"
                    currentBuild.result = 'FAILURE'
                    error ("Failed executing command -> ${err}")
                }
            }
        }
    }
}