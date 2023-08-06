import java.time.LocalDate;

def call() {
    node {
        environment{
            HOST_NAME
            HOST_IP
            HOST_USER
            HOST_PASSWORD
        }

        def remote = [:]
        String fileName

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
                remote.name = 'rdvl-server'
                remote.host = HOST_IP
                remote.user = HOST_USER
                remote.password = HOST_PASSWORD
                remote.port = 22
                remote.allowAnyHosts = true

                // Define file name
                LocalDate date = LocalDate.now();
                fileName = "gallery_backup_" + date.toString().replace('-', '_')

                // Build display name
                currentBuild.displayName = "${HOST_NAME}: Backup - ${fileName}"
            }

            stage('Create Backup') {
                // Get actual date and time
                script {
                    // Execute command
                    sshCommand(
                        remote: remote,
                        command: "tar -czvf /DATA/Backups/Gallery/${fileName}.tar.gz /DATA/Gallery")
                }
            }

            stage('Delete Old Backups') {
                script {
                    // Execute command
                    sshCommand(
                        remote: remote,
                        command: "find /DATA/Backups/Gallery/ ! -name ${fileName}.tar.gz -type f -exec rm -f {} +")
                }
                currentBuild.description = "${HOST_NAME}: Success"
            }

        } catch (Exception err) {
            // Build failed
            currentBuild.description = "${HOST_NAME}: Failed executing command -> ${err}"
            currentBuild.result = 'FAILURE'
            error ("Failed executing command -> ${err}")
        }
    }
}