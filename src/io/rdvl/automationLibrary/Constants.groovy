package io.rdvl.automationLibrary

class Constants {
    static final String repoURL = 'https://github.com/R-dVL/automationLibrary.git'

    Constants () {}

    @Override
    @NonCPS
    public String toString() {
        return """
            Repository URL: ${repoURL}
        """
    }
}