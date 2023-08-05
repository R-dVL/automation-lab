def call() {
    stage('Demo') {
        script {
            def remote = [:]
            remote.host = IP
            remote.user = CREDENTIALS
            remote.password = CREDENTIALS
            remote.port = '22'
            remote.allowAnyHosts = true
            sshCommand remote: remote, command: "pwd"
        }
    }
}