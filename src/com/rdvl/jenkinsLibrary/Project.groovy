package com.rdvl.jenkinsLibrary

public class Project {
    // Pipeline Context
    private def steps

    // Project Params
    private String name
    private String version
    private String url
    private String credentialsId
    private String user
    private String password
    private String playbook

    Project(steps, name, version) {
        // Pipeline Context
        this.steps = steps

        // Basic Params
        this.name = name
        this.version = version
        this.url = steps.configuration.projects."${name}".url
        this.credentialsId = steps.configuration.projects."${name}".credentials != null ? steps.configuration.projects."${name}".credentials : null
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
        return """'{"project": {"name": "${name}", "version": "${version}", "url": "${url}", "user": "${user}", "password": "${password}"}}'"""
    }
}
