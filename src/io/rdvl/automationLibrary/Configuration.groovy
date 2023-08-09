package io.rdvl.automationLibrary

import groovy.json.JsonSlurper

class Configuration {
    private def configuration

    Configuration () {
        def jsonSlurper = new JsonSlurper()
        this.configuration = jsonSlurper.parse(new File('/var/jenkins_home/workspace/Test-Pipeline/automation-lab/resources/static_configuration.json'))
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