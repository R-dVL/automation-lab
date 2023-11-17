package com.rdvl.jenkinsLibrary
/**
 * Represents a remote host in the context of a Jenkins pipeline.
 */
class Host implements Serializable {
    // Pipeline Context
    private def steps
    private def utils

    // Default Params
    private String name
    private String ip
    private String credentialsId
    private String user
    private String password

    /**
     * Constructor for the Host class.
     *
     * @param steps The Jenkins pipeline steps context.
     * @param hostName The name of the host.
     */
    Host(steps, hostName) {
        // Pipeline context setup
        this.steps = steps
        this.utils = steps.utils

        // Host setup
        this.name = hostName
        this.ip = steps.configuration.hosts."${name}".ip
        this.credentialsId = steps.configuration.hosts."${name}".credentials
    }

    /**
     * Initializes the host by retrieving credentials.
     */
    def init() {
        def credentials = utils.retrieveCredentials(credentialsId)
        this.user = credentials.user
        this.password = credentials.password
    }

    /**
     * Executes an SSH command on the remote host.
     *
     * @param command The command to be executed.
     * @param sudo Indicates whether to use sudo for the command. Default is false.
     * @return The result of the SSH command.
     */
    @NonCPS
    def sshCommand(command, sudo = false) {
        // Remote params
        def remote = [:]
        remote.name = name
        remote.host = ip
        remote.user = user
        remote.password = password
        remote.port = 22
        remote.allowAnyHosts = true

        // Execute command
        return steps.sshCommand(remote: remote, command: command, sudo: sudo)
    }

    /**
     * Uploads a file to the remote host using SSH.
     *
     * @param file The local file to be uploaded.
     * @param remotePath The path on the remote host to upload the file to.
     */
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

    /**
     * Downloads a file from the remote host using SSH.
     *
     * @param path The local path to save the downloaded file.
     * @param remotePath The path on the remote host to download the file from.
     */
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
        return ip
    }

    @NonCPS
    def getName() {
        return name
    }

    @NonCPS
    def getCredentialsId() {
        return credentialsId
    }

    /**
     * Overrides the toString() method to return a JSON representation of the Host object.
     *
     * @return A JSON representation of the Host object.
     */
    @Override
    @NonCPS
    public String toString() {
        return """
        '{
            "host": {
                "name": "${name}",
                "ip": "${ip}",
                "credentialsId": "${credentialsId}",
                "user": "${user}",
                "password": "${password}"
            }
        }'
    """
    }
}