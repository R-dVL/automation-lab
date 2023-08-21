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
        switch(tech) {
            pipeline.checkout(scm: [$class: 'GitSCM', userRemoteConfigs: [[url: url, credentialsId: 'github-login-credentials']], branches: [[name: version]]],poll: false)

            def mvnHome = pipeline.tool name: 'Maven 3.9.4', type: 'maven'
            def mvnCmd = "${mvnHome}/bin/mvn"

            pipeline.sh "${mvnCmd} clean package"
        }
    }

    def deploy() {
            Nexus nexus = new Nexus(this)
            nexus.uploadArtifact(artifactId, version, artifactId, 'jar')
        }
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
