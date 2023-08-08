package io.rdvl.automationLibrary

class Configuration {
    private def configuration

    Configuration () {
        this.configuration = readJSON(file: "resources/static_configuration.json")
    }

    def getConfiguration() {
        return this.configuration
    }
}
