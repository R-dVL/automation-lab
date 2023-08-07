package io.rdvl.automationLibrary

import groovy.json.JsonSlurperClassic

class Configuration {
    private def configuration

    Configuration () {
        this.configuration = new JsonSlurperClassic().parseText(steps.libraryResource('static_configuration.json'))
    }

    def getConfiguration() {
        return this.configuration
    }
}