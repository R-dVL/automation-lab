import java.time.LocalDateTime

def call() {
    node {
        environment{
            HOST_NAME
            HOST_IP
            HOST_USER
            HOST_PASSWORD
            REMOTE
            FILE_NAME
        }

        try {
            stage('Configure Host') {
                // Configure env vars in function of the host selected
                HOST_NAME = 'rdvl-server'

                withCredentials([
                    usernamePassword(
                        credentialsId: 'server-credentials',
                        usernameVariable: 'user',
                        passwordVariable: 'password')
                ]) {
                    HOST_USER = user
                    HOST_PASSWORD = password
                }

                withCredentials([
                    string(
                        credentialsId: 'server-ip',
                        variable: 'ip',)
                ]) {
                    HOST_IP = ip
                }
                REMOTE.name = 'rdvl-server'
                REMOTE.host = HOST_IP
                REMOTE.user = HOST_USER
                REMOTE.password = HOST_PASSWORD
                REMOTE.port = 22
                REMOTE.allowAnyHosts = true

                // Define file name
                def dt = LocalDateTime.now()
                FILE_NAME = "gallery_backup_" + dt

            stage('Create Backup') {
                // Get actual date and time
                script {
                    // Execute command
                    sshCommand(
                        remote: remote,
                        command: "tar -czvf /DATA/Backups/Gallery/${FILE_NAME}.tar.gz /DATA/Gallery")
                }
            }

            stage('Delete Old backups') {
                script {
                    def file = 'test.tar.gz'
                    // Execute command
                    sshCommand(
                        remote: remote,
                        command: "find /DATA/Backups/Gallery/ ! -name ${fileFILE_NAMEName}.tar.gz -type f -exec rm -f {} +")
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