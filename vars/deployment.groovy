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
                script {
                    // Configuración de la publicación mediante SSH
                    def sshPublisher = sshPublisher(
                            publishers: [
                                    sshPublisherDesc(
                                            configName: 'server', // Nombre de la configuración SSH previamente definida
                                            transfers: [
                                                    sshTransfer(
                                                            sourceFiles: 'target/cat-watcher_v1.0.0.tar.gz', // Ruta local del archivo a transferir
                                                            removePrefix: 'target/', // Prefijo a eliminar en la ruta de destino remota
                                                            remoteDirectory: '/home/rdvl/cat-watcher/artifacts' // Ruta en el servidor remoto
                                                    )
                                                    // Puedes agregar más transferencias aquí si es necesario
                                            ]
                                    )
                            ]
                    )
                    sshPublisher.perform(build, launcher, listener)
                }
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}