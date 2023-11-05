package com.rdvl.jenkinsLibrary

public class Project {
    // Pipeline Context
    private def steps

    // Project Params
    private String name
    private String version
    private String url
    private String user
    private String password

    Project(steps, name, version) {
        // Pipeline Context
        this.steps = steps

        // Basic Params
        this.name = name
        this.version = version
        this.url = steps.configuration.projects."${name}".url
    }

    def init() {
        def credentials = steps.utils.retrieveCredentials('mongo-credentials')
        this.user = credentials.user
        this.password = credentials.password
    }

    @NonCPS
    def getDeploymentTech() {
        return this.deploymentTech
    }

    @Override
    @NonCPS
    public String toString() {
        return """'{"project": {"name": "${name}", "version": "${version}", "url": "${url}"}, "user": "${user}", "password": ${password}}}'"""
    }
}
