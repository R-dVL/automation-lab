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
        this.ip = config.Hosts."${name}".ip
        this.credentials = config.Hosts."${name}".credentials
    }

    def getName() {
        return this.name
    }

    def getCredentials() {
        return this.credentials
    }

    def getConfig() {
        return this.config
    }

    @Override
    public String toString() {
        return "Host"
    }
}