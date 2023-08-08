package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Configuration {
    private def configuration

    Configuration () {
        def jsonSlurper = new JsonSlurper()
        this.configuration = jsonSlurper.parse(new File('${WORKSPACE}/automation-lab/resources/static_configuration.json'))
    }

    def getConfiguration() {
        return this.configuration
    }
}
