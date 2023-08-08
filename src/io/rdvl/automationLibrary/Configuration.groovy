package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Configuration {
    private def configuration

    Configuration () {
        def jsonSlurper = new JsonSlurper()
        this.configuration = jsonSlurper.parse(new File('./conf.json'))
    }

    def getConfiguration() {
        return this.configuration
    }
}
