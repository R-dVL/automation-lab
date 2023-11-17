package com.rdvl.jenkinsLibrary

public class Project {
    // Pipeline Context
    private def steps
    private def utils

    // Project Params
    private String name
    private String version
    private String url
    private String database
    private String uri
    private String credentialsId
    private String user
    private String password

    Project(steps, name, version) {
        // Pipeline Context
        this.steps = steps
        this.utils = steps.utils

        // Basic Params
        this.name = name
        this.version = version
        this.url = steps.configuration.projects."${name}".url

        // Some projects such as frontend doesn't have a related database
        this.database = steps.configuration.projects."${name}".database != null ? steps.configuration.projects."${name}".database.name : null
        this.uri = database != null ? steps.configuration.projects."${name}".database.uri : null
        this.credentialsId = database != null ? steps.configuration.projects."${name}".database.credentials : null
    }

    def init() {
        if (credentialsId != null) {
            def credentials = utils.retrieveCredentials(credentialsId)
            this.user = credentials.user
            this.password = credentials.password
        } else {
            this.user = null
            this.password = null
        }

    }

    @NonCPS
    def getName() {
        return name
    }

    @NonCPS
    def getVersion() {
        return version
    }

    // toString() method override to get a JSON of the class
    @Override
    @NonCPS
    public String toString() {
        return """
        '{
            "project": {
                "name": "${name}",
                "version": "${version}",
                "url": "${url}",
                "user": "${user}",
                "password": "${password}",
                "database": "${database}",
                "uri": "${uri}"
            }
        }'
    """
    }
}
