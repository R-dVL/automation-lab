import groovy.json.JsonSlurperClassic

class Configuration {
    private def configuration
    static final String path = 'resources/static_configuration.json'

    Configuration () {
        this.configuration = new JsonSlurperClassic().parseText(steps.libraryResource('static_configuration.json'))
    }

    def getConfiguration() {
        return this.configuration
    }
}