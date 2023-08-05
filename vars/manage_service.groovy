def call() {
    node {
        stage('Check Service Status') {
            script {
                currentBuild.displayName = "#${HOST_NAME}: ${SERVICE} - ${COMMAND}"

                def remote = [:]
                remote.name = HOST_NAME
                remote.host = HOST_IP
                remote.user = HOST_USER
                remote.password = HOST_PASSWORD
                remote.port = 22
                remote.allowAnyHosts = true

                try {
                    sshCommand remote: remote, command: "systemctl ${COMMAND} ${SERVICE}", sudo: false
                    currentBuild.description = "#${HOST_NAME}: Success"
                } catch (Exception err) {
                    error ("Failed executing command -> ${err}")
                    currentBuild.description = "#${HOST_NAME}: Failed"
                }
            }
        }
    }
}