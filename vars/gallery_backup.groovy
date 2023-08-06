def call() {
    node {
        try {
            stage('Execute Command') {
                script {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'server-credentials',
                            usernameVariable: 'user',
                            passwordVariable: 'password')
                    ]) {
                        // Remote params
                        def remote = [:]
                        remote.name = 'rdvl-server'
                        remote.host = '212.169.220.230'
                        remote.user = user
                        remote.password = password
                        remote.port = 22
                        remote.allowAnyHosts = true

                        // Execute command
                        sshCommand(
                            remote: remote,
                            command: "tar cf - /DATA/Gallery/bak -P | pv -s $(du -sb /Gallery-Backup | awk '{print $1}') | gzip > /DATA/Backups/Gallery/test2.tar.gz"
                            //command: "tar -czvf /DATA/Backups/Gallery/test.tar.gz /DATA/Gallery")
                    }
                }
            }

        } catch (Exception err) {
            // Build failed
            currentBuild.result = 'FAILURE'
            error ("Failed executing command -> ${err}")
        }
    }
}