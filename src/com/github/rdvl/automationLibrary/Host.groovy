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
        this.configIp = pipeline.cfg.hosts."${name}".ip
        this.configCredentials = pipeline.cfg.hosts."${name}".credentials
    }

    @NonCPS
    def sshCommand(cmd, sudo = false) {
        // Remote params
        def remote = [:]
        remote.name = name
        remote.host = ip
        remote.user = user
        remote.password = password
        remote.port = 22
        remote.allowAnyHosts = true

        // Execute command
        pipeline.sshCommand remote: remote, command: cmd, sudo: sudo
    }

    @NonCPS
    def sshPut(file, remotePath) {
        // Remote params
        def remote = [:]
        remote.name = name
        remote.host = ip
        remote.user = user
        remote.password = password
        remote.port = 22
        remote.allowAnyHosts = true

        // Execute command
        pipeline.sshPut remote: remote, from: file, into: remotePath
    }

    @NonCPS
    def getIp() {
        return this.ip
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
    def getUser() {
        return this.user
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