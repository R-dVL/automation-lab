package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Configuration {
    // Default params
    private def configuration
    static final String configurationPath = steps.WORKSPACE + '/automation-lab/resources/configuration.json'

    Configuration () {
        // Parse configuration json
        def jsonSlurper = new JsonSlurper()
        this.configuration = jsonSlurper.parse(new File(configurationPath))
    }

    @NonCPS
    def getConfiguration() {
        return this.configuration
    }

    @Override
    @NonCPS
    public String toString() {
        return "JSON: ${configuration}"
    }
}