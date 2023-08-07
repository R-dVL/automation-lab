import groovy.json.JsonSlurper

class Configuration {
    public def configuration
    static final String path = 'resources/static_configuration.json'

    Configuration () {
        def jsonSlurper = new JsonSlurper()
        this.configuration = jsonSlurper.parse(new File(path))
    }

    def getConfiguration() {
        return this.configuration
    }
}