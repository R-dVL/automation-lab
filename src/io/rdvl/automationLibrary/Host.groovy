package io.rdvl.automationLibrary

class Host {
    // Default Params
    private String name
    private String credentials
    private String ip
    private String user
    private String password
    private def configuration

    Host(hostName, config) {
        // Retrieve configuration
        this.configuration = config

        // Name selected when constructed
        this.name = hostName
        
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
    public def getUser(){
        return this.user
    }

    @NonCPS
    public def getPassword(){
        return this.password
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