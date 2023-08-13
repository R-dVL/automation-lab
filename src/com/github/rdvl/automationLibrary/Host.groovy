package com.github.rdvl.automationLibrary

class Host implements Serializable {
    // Pipeline Context
    private def pipeline

    // Default Params
    private String name
    private String configCredentials
    private String configIp
    private String ip
    private String user
    private String password

    Host(pipeline, hostName) {
        // Pipeline context setup
        this.pipeline = pipeline

        // Host selected
        this.name = hostName

        // Get params from configuration
        switch(name){
            case 'Server':
                this.configIp = pipeline.configuration.Hosts.Server.Ip
                this.configCredentials = pipeline.configuration.Hosts.Server.Credentials
                break

            case 'RPi':
                this.configIp = pipeline.configuration.Hosts.RPi.Ip
                this.configCredentials = pipeline.configuration.Hosts.RPi.Credentials
                break

            default:
                pipeline.error("${name} | Not defined in Configuration file")
                break
        }
    }

    @NonCPS
    def sshCommand(cmd) {
        // Remote params
        def remote = [:]
        remote.name = name
        remote.host = ip
        remote.user = user
        remote.password = password
        remote.port = 22
        remote.allowAnyHosts = true

        // Execute command
        pipeline.sshCommand remote: remote, command: cmd, sudo: pipeline.SUDO
    }

    @NonCPS
    def getConfigIp() {
        return this.configIp
    }

    @NonCPS
    def getConfigCredentials() {
        return this.configCredentials
    }

    @NonCPS
    def setUser(user) {
        this.user = user
    }

    @NonCPS
    def setPassword(password) {
        this.password = password
    }

    @NonCPS
    def setIp(ip) {
        this.ip = ip
    }

    @Override
    @NonCPS
    public String toString() {
        return """
            Name: ${name}
            Config IP: ${configIp}
            Credentials: ${configCredentials}
            User: ${user}
            Password: ${password}
            IP: ${ip}
        """
    }
}