package com.rdvl.jenkinsLibrary

public class Project {
    // Pipeline Context
    private def steps

    // Project Params
    private String name
    private String version
    private String artifactId
    private String url
    private String techName
    private def deploymentTech

    Project(steps, name, version) {
        // Pipeline Context
        this.steps = steps

        // Basic Params
        this.name = name
        this.version = version
        this.artifactId = name + "-" + version
        this.url = steps.configuration.projects."${name}".url
        this.techName = steps.configuration.projects."${name}".tech

        // Deployment tech construction
        switch(techName) {
            case 'maven':
                this.deploymentTech = new TechMVN(steps, name, version, artifactId, url)
                break

            case 'npm':
                this.deploymentTech = new TechNPM(steps, name, version, artifactId, url)
                break

            case 'python':
                this.deploymentTech = new TechPY(steps, name, version, artifactId, url)
                break

            default:
                steps.error("${name} | Tech not defined.")
                break
        }
    }

    @NonCPS
    def getDeploymentTech() {
        return this.deploymentTech
    }

    @Override
    @NonCPS
    public String toString() {
        return """
            Name: ${name}
            Version: ${version}
            Artifact: ${artifactId}
            Url: ${url}
            Destination: ${destination}
            Tech: ${tech}
        """
    }
}
