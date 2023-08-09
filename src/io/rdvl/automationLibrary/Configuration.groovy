package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Configuration {
    // Default params
    private def configuration
    private String configurationPath
    private String workspace

    Configuration () {
        // Parse configuration json
        def jsonSlurper = new JsonSlurper()
        this.workspace = System.getenv('WORKSPACE')
        this.configurationPath = workspace + "/automation-lab/resources/configuration.json"
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