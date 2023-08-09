package io.rdvl.automationLibrary

class Host {
    // Default Params
    private String name
    private String credentials
    private String ip
    private String user
    private String password
    private def config

    Host(hostName) {
        // Retrieve configuration
        Configuration configJson = new Configuration()
        this.config = configJson.getConfiguration()

        // Name selected when constructed
        this.name = hostName
        
        // Get params from configuration
        switch(name){
            case 'Server':
                this.ip = config.Hosts."${name}".Ip
                this.credentials = config.Hosts."{name}".Credentials
                break

            case 'RPi':
                this.ip = config.Hosts.RPi.Ip
                this.credentials = config.Hosts.RPi.Credentials
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
    public def getConfig(){
        return this.config
    }

    @Override
    @NonCPS
    public String toString() {
        return "Name: ${name}\nIP: ${ip}\nCredentials: ${credentials}"
    }
}