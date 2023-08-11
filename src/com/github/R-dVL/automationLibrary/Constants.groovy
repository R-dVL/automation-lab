package com.github.R-dVL.automationLibrary

class Constants {
    // Singleton class to store constants
    private static Constants instance

    // Constants
    static final String repoURL = 'https://github.com/R-dVL/automationLibrary.git'
    static final String configPath = '/resources/configuration.json'

    // Singleton constructor
    private Constants () {}

    @NonCPS
    static Constants getInstance() {
        if (instance == null) {
            instance = new Constants()
        }
        return instance
    }

    @Override
    @NonCPS
    public String toString() {
        return """
            Repository URL: ${repoURL}
        """
    }
}