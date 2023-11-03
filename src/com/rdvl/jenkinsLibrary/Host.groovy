package com.rdvl.jenkinsLibrary

class Host implements Serializable {
    // Pipeline Context
    private def steps

    // Default Params
    private String name
    private String ip
    private String credentials
    private String user
    private String password

    Host(steps, hostName) {
        // Pipeline context setup
        this.steps = steps

        // Host setup
        this.name = hostName
        this.ip = steps.configuration.hosts."${name}".ip
        this.credentials = steps.configuration.hosts."${name}".credentials
        steps.print(credentials)
        def credentials = steps.utils.retrieveCredentials(credentials)
        this.user = credentials.user
        this.password = credentials.password
    }

    // Jenkins ssh Command wrapper
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
        steps.sshCommand remote: remote, command: cmd, sudo: sudo
    }

    // Jenkins ssh Put wrapper
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
        steps.sshPut remote: remote, from: file, into: remotePath
    }

    // Jenkins ssh Get wrapper
    @NonCPS
    def sshGet(path, remotePath) {
        def remote = [:]
        remote.name = name
        remote.host = ip
        remote.user = user
        remote.password = password
        remote.allowAnyHosts = true

        steps.sshGet remote: remote, from: remotePath, into: path, override: true
    }

    @NonCPS
    def getIp() {
        return this.ip
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