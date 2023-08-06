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
                            command: "'
                                FROMSIZE=`du -sk --apparent-size ${FROMPATH} | cut -f 1`;
                                CHECKPOINT=`echo ${FROMSIZE}/50 | bc`;
                                echo "Estimated: [==================================================]";
                                echo -n "Progess:   [";
                                tar -c --record-size=1K --checkpoint="${CHECKPOINT}" --checkpoint-action="ttyout=>" -f - "${FROMPATH}" | bzip2 > "${TOFILE}";
                                echo "]"
                            '"
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