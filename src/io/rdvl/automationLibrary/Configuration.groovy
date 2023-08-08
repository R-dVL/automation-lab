package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Configuration {
    private def configuration

    Configuration () {
        def jsonSlurper = new JsonSlurper()
        this.configuration = jsonSlurper.parse(new File('./config.json'))
    }

    def getConfiguration() {
        return this.configuration
    }
}
