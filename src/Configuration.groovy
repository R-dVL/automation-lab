import groovy.json.JsonSlurper

class Configuration {
    private def configuration
    static final String path = 'resources/static_configuration.json'
}

def Configuration () {
    def jsonSlurper = new JsonSlurper()
    this.configuration = jsonSlurper.parse(new File(path))
}

def getConfiguration() {
    return this.configuration
}