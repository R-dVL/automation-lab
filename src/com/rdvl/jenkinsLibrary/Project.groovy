package com.rdvl.jenkinsLibrary
/**
 * Represents a project in the context of a Jenkins pipeline.
 */
public class Project {
    // Pipeline Context
    private def steps
    private def utils

    // Project Params
    private String name
    private String version
    private String url
    private String artifactName

    /**
     * Constructor for the Project class.
     *
     * @param steps The Jenkins pipeline steps context.
     * @param name The name of the project.
     * @param version The version of the project.
     */
    Project(steps, name, version) {
        // Pipeline Context
        this.steps = steps
        this.utils = steps.utils

        // Basic Params
        this.name = name
        this.version = version
        this.url = steps.configuration.projects."${name}".url
        this.artifactName = steps.configuration.projects."${name}".artifact_name
    }

    @NonCPS
    def getName() {
        return name
    }

    @NonCPS
    def getVersion() {
        return version
    }

    @NonCPS
    def getUrl() {
        return url
    }

    @NonCPS
    def getArtifactName() {
        return artifactName
    }

    /**
     * Overrides the toString() method to return a JSON representation of the Project object.
     *
     * @return A JSON representation of the Project object.
     */
    @Override
    @NonCPS
    public String toString() {
        return """
        '{
            "project": {
                "name": "${name}",
                "version": "${version}",
                "url": "${url}",
                "artifactName": "${artifactName}",
            }
        }'
    """
    }
}
