package com.github.rdvl.automationLibrary;

public class Project {
    // Pipeline Context
    private def pipeline

    // Project Params
    private String name
    private String version
    private String artifactId
    private String url
    private def destination
    private String techName
    private def deploymentTech

    Project(pipeline, name, version) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = name + "_" + version
        this.url = pipeline.cfg.projects."${name}".url
        this.destination = pipeline.cfg.projects."${name}".destination
        this.techName = pipeline.cfg.projects."${name}".tech

        switch(techName) {
            case 'maven':
                TechMVN mvn = new TechMVN(pipeline, name, version, artifactId, url, destination)
                break

            case 'npm':
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
        """;
    }
}
