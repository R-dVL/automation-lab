def call() {
    stage('Demo') {
        script {
            def remote = [:]
            remote.name = SERVER
            remote.host = IP
            remote.user = USER
            remote.password = PASSWORD
            remote.port = '22'
            remote.allowAnyHosts = true
            sshCommand remote: remote, command: "pwd"
        }
    }
}