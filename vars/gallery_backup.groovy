import java.time.LocalDateTime

def call() {
    node {
        try {
/*
            stage('Create Backup') {
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

                        // Get actual date and time
                        def dt = LocalDateTime.now()

                        // Execute command
                        sshCommand(
                            remote: remote,
                            command: "tar -czvf /DATA/Backups/Gallery/${dt}.tar.gz /DATA/Gallery")
                    }
                }
            }
*/
            stage('Delete Old backups') {
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
                        def dt = 'test'
                        // Execute command
                        sshCommand(
                            remote: remote,
                            command: "'
                                cd /DATA/Backups/Gallery
                                find . ! -name "${dt}.tar.gz" -type f -exec rm -f {} \;
                            '")
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