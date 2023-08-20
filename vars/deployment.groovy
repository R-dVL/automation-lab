package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            Project prj = new Project(this, NAME, VERSION)
            Host host = new Host(this, 'server')

            stage('Prepare') {
                cleanWs()

                prj.prepare()
            }

            stage('Host Setup') {
                script {
                    // User & Password
                    withCredentials([
                            usernamePassword(credentialsId: host.getConfigCredentials(), usernameVariable: 'user', passwordVariable: 'password')]) {
                        host.setUser(user)
                        host.setPassword(password)
                    }
                    // IP
                    withCredentials([
                            string(credentialsId: host.getConfigIp(), variable: 'ip')]) {
                        host.setIp(ip)
                    }
                }
            }

            stage('Deploy') {
                withCredentials([usernamePassword(credentialsId: 'server-credentials', usernameVariable: 'user', passwordVariable: 'password')]) {
                    // Definir los parámetros
                    def localFile = '/target/cat-watcher_v1.0.0.tar.gz'
                    def remoteUser = user
                    def remoteHost = '192.168.1.55'
                    def remotePath = '/home/rdvl/'

                    // Crear el script sftp con contraseña y ejecutarlo
                    def sftpScript = "spawn sftp ${remoteUser}@${remoteHost}\nexpect \"password:\"\nsend \"${password}\\n\"\nexpect \"sftp>\"\nsend \"put ${localFile} ${remotePath}\\n\"\nexpect \"sftp>\"\nsend \"bye\\n\"\ninteract"
                    writeFile file: 'sftp_script.exp', text: sftpScript
                    sh "expect sftp_script.exp"
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}