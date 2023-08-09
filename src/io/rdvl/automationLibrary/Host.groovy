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
        this.ip = config.Hosts.'${name}'.Ip
        this.credentials = config.Hosts.'${name}'.Credentials
    }

    @Override
    @NonCPS
    public String toString() {
        return "Name: ${name}\nIP: ${ip}\nCredentials: ${credentials}"
    }
}