def call() {
    node {
        stage('Demo') {
            script {
                def remote = [:]
                remote.name = HOST_NAME
                remote.host = HOST_IP
                remote.user = HOST_USER
                remote.password = HOST_PASSWORD
                remote.port = 22
                remote.allowAnyHosts = true
                remote.sudo = true
                sshCommand remote: remote, command: "apt update && apt upgrade"
            }
        }
    }
}