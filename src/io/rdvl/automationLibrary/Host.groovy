package io.rdvl.automationLibrary

class Host {
    private String name
    private String credentials
    private String ip
    private String user
    private String password
    private def config

    Host(hostName) {
        Configuration configJson = new Configuration()
        this.config = configJson.getConfiguration()
        this.name = hostName
        switch(name){
            case 'Server':
                this.ip = config.Hosts.Server.Ip
                this.credentials = config.Hosts.Server.Credentials
            case 'RPi':
                this.ip = config.Hosts.RPi.Ip
                this.credentials = config.Hosts.RPi.Credentials
        }
    }

    @Override
    @NonCPS
    public String toString() {
        return "Name: ${name}\nIP: ${ip}\nCredentials: ${credentials}"
    }
}