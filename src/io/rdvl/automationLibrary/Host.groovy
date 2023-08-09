package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Host implements Serializable {
    // Pipeline Context
    private def pipeline

    // Default Params
    private String name
    private String credentials
    private String ip
    private String user
    private String password
    private def configuration

    Host(pipeline, hostName) {
        // Pipeline context setup
        this.pipeline = pipeline

        // Retrieve configuration
        def jsonSlurper = new JsonSlurper()
        this.configuration =  jsonSlurper.parse(new File("${pipeline.WORKSPACE}/automation-lab/resources/configuration.json"))
        pipeline.print("Configuration: " + configuration)

        // Host selected
        this.name = hostName
        pipeline.print("Host Name: " + name)
        
        // Get params from configuration
        switch(name){
            case 'Server':
                this.ip = configuration.Hosts.Server.Ip
                this.credentials = configuration.Hosts.Server.Credentials
                break

            case 'RPi':
                this.ip = configuration.Hosts.RPi.Ip
                this.credentials = configuration.Hosts.RPi.Credentials
                break
            
            default:
                pipeline.error("${name} | Not defined in Configuration file")
                break
        }

        // Retrieve info from Jenkins
        pipeline.script {
            // User & Password
            pipeline.withCredentials([
                pipeline.usernamePassword(credentialsId: 'server-credentials', usernameVariable: 'user', passwordVariable: 'password')]) {
                    this.user = user
                    this.password = password
            }
            // IP
            pipeline.withCredentials([
                pipeline.string(credentialsId: 'server-ip', variable: 'ip')]) {
                    this.ip = ip
            }
        }
        }
    }

    def sshCommand(cmd){
        pipeline.script {
            // Remote params
            def remote = [
                name = name
                host = ip
                user = user
                password = password
                port = 22
                allowAnyHosts = true
            ]

            // Execute command
            pipeline.sshCommand remote: remote, command: cmd, sudo: false
        }
    }

    @NonCPS
    public def getName(){
        return this.name
    }

    @NonCPS
    public def getCredentials(){
        return this.credentials
    }

    @NonCPS
    public def getIp(){
        return this.ip
    }

    @NonCPS
    public def setIp(ip){
        this.ip
    }

    @NonCPS
    public def getUser(){
        return this.user
    }

    @NonCPS
    public def setUser(user){
        this.user
    }

    @NonCPS
    public def getPassword(){
        return this.password
    }

    @NonCPS
    public def setPassword(){
        this.password
    }

    @NonCPS
    public def getConfiguration(){
        return this.configuration
    }

    @Override
    @NonCPS
    public String toString() {
        return "Name: ${name}\nIP: ${ip}\nCredentials: ${credentials}"
    }
}