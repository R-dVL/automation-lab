package com.rdvl.jenkinsLibrary

import com.rdvl.jenkinsLibrary.technologies.Docker
import com.rdvl.jenkinsLibrary.technologies.Golang

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
    private String techName

    // Technology
    private def technology

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
        this.techName = steps.configuration.projects."${name}".tech_name

        // Technology
        switch(techName) {
            case 'docker':
                this.technology = new Docker(this)
                break

            case 'golang':
                this.technology = new Golang(this)
                break

            default:
                utils.log("Technology not configured.", "red")
                break
        }
    }

    @NonCPS
    String getName() {
        return name
    }

    @NonCPS
    String getVersion() {
        return version
    }

    @NonCPS
    String getUrl() {
        return url
    }

    @NonCPS
    String getArtifactName() {
        return artifactName
    }

    @NonCPS
    String getTechName() {
        return techName
    }

    @NonCPS
    def getTechnology() {
        return technology
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
