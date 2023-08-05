def call() {
    stages {
        stage('Demo') {
            script {
                def remote = [:]
                remote.name = 'server'
                remote.host = '212.169.220.230'
                remote.user = 'rdvl'
                remote.password = 'cocotero09'
                remote.port = '22'
                remote.allowAnyHosts = true
                sshCommand remote: remote, command: "pwd"
            }
        }
    }
}