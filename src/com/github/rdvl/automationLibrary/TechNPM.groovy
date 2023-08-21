package com.github.rdvl.automationLibrary;

public class TechMVN {
    // Pipeline Context
    private def pipeline

    // Tech
    private String name
    private String version
    private String artifactId
    private String url
    private def destination
    private String tech

    TechMVN(pipeline, name, version, artifactId, url, destination) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.url = url
        this.destination = destination
    }

    def prepare() {

    }

    def deploy() {
        Nexus nexus = new Nexus(pipeline)
        nexus.uploadArtifact(artifactId, version, artifactId, '.package')
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
