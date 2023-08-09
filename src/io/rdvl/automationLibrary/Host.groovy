package io.rdvl.automationLibrary

import groovy.json.JsonSlurperClassic

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
    private def configuration

    Host(pipeline, hostName) {
        // Pipeline context setup
        this.pipeline = pipeline

        // Retrieve configuration
        def jsonSlurperClassic = new JsonSlurperClassic()
        this.configuration =  jsonSlurperClassic.parse(new File("${pipeline.WORKSPACE}/automation-lab/resources/configuration.json"))
        pipeline.print("Configuration: " + configuration)

        // Host selected
        this.name = hostName
        pipeline.print("Host Name: " + name)
        
        // Get params from configuration
        switch(name){
            case 'Server':
                this.configIp = configuration.Hosts.Server.Ip
                this.configCredentials = configuration.Hosts.Server.Credentials
                break

            case 'RPi':
                this.configIp = configuration.Hosts.RPi.Ip
                this.configCredentials = configuration.Hosts.RPi.Credentials
                break
            
            default:
                pipeline.error("${name} | Not defined in Configuration file")
                break

            // Retrieve info from Jenkins
            // User & Password
            pipeline.withCredentials([
                pipeline.usernamePassword(credentialsId: configCredentials, usernameVariable: 'user', passwordVariable: 'password')]) {
                    this.user = user
                    this.password = password
            }

            // IP
            pipeline.withCredentials([
                pipeline.string(credentialsId: configIp, variable: 'ip')]) {
                    this.ip = ip
            }
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
        pipeline.sshCommand remote: remote, command: cmd, sudo: false
    }

    @Override
    @NonCPS
    public String toString() {
        return "Name: ${name}\nIP: ${configIp}\nCredentials: ${configCredentials}\nUser: ${user}\nPassword: ${password}\nIP: ${ip}"
    }
}