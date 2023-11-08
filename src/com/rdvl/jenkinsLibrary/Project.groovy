package com.rdvl.jenkinsLibrary

public class Project {
    // Pipeline Context
    private def steps

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

        // Basic Params
        this.name = name
        this.version = version
        this.url = steps.configuration.projects."${name}".url
        this.database = steps.configuration.projects."${name}".database.name
        this.uri = steps.configuration.projects."${name}".database.uri
        this.credentialsId = steps.configuration.projects."${name}".database.credentials
    }

    def init() {
        if (credentialsId != null) {
            def credentials = steps.utils.retrieveCredentials(credentialsId)
            this.user = credentials.user
            this.password = credentials.password
        } else {
            this.user = null
            this.password = null
        }

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
