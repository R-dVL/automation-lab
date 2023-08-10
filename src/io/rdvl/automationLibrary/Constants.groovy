package io.rdvl.automationLibrary

class Constants {
    static final String repoURL = 'https://github.com/R-dVL/automationLibrary.git'
    static final String configPath = '/automationLibrary/resources/configuration.json'

    Constants () {}

    @Override
    @NonCPS
    public String toString() {
        return """
            Repository URL: ${repoURL}
        """
    }
}