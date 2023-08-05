def call() {
    node {
        stage('Check Service Status') {
            script {
                def remote = [:]
                remote.name = HOST_NAME
                remote.host = HOST_IP
                remote.user = HOST_USER
                remote.password = HOST_PASSWORD
                remote.port = 22
                remote.allowAnyHosts = true
                sshCommand remote: remote, command: "systemctl ${COMMAND} ${SERVICE}", sudo: false
            }
        }
    }
}