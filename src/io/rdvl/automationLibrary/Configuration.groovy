package io.rdvl.automationLibrary

class Configuration {
    private def configuration

    Configuration () {
        this.configuration = readJSON file: './automation.lab/resources/static_configuration.json'
    }

    def getConfiguration() {
        return this.configuration
    }
}
