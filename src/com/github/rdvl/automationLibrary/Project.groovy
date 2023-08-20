package com.github.rdvl.automationLibrary;

public class Project {
    private def pipeline;
    private String name;
    private String version;
    private String artifactId;
    private String url;
    private def destination;

    Project(pipeline, name, version) {
        this.pipeline = pipeline;
        this.name = name;
        this.version = version;
        this.artifactId = name + "_" + version;
        this.url = pipeline.cfg.projects."${name}".url;
        this.destination = pipeline.cfg.projects."${name}".destination;
    }

    def downloadCode() {
        pipeline.checkout(scm: [$class: 'GitSCM', userRemoteConfigs: [[url: url, credentialsId: 'github-token']], branches: [[name: version]]],poll: false)
    }

    @NonCPS
    def getArtifactId() {
        return this.artifactId
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
        """;
    }
}
