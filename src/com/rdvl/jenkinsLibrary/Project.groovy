package com.rdvl.jenkinsLibrary

public class Project {
    // Pipeline Context
    private def pipeline

    // Project Params
    private String name
    private String version
    private String artifactId
    private String nexusRepository
    private String url
    private String techName
    private def deploymentTech

    Project(pipeline, name, version) {
        // Pipeline Context
        this.pipeline = pipeline

        // Basic Params
        this.name = name
        this.version = version
        this.artifactId = name + "-" + version
        this.url = pipeline.configuration.projects."${name}".url
        this.techName = pipeline.configuration.projects."${name}".tech

        // Deployment tech construction
        switch(techName) {
            case 'maven':
                this.deploymentTech = new TechMVN(pipeline, name, version, artifactId, url)
                break

            case 'npm':
                this.deploymentTech = new TechNPM(pipeline, name, version, artifactId, url)
                break

            case 'python':
                this.deploymentTech = new TechPY(pipeline, name, version, artifactId, url)
                break

            default:
                pipeline.error("${name} | Tech not defined.")
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
