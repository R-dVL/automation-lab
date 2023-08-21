package com.github.rdvl.automationLibrary;

public class TechMVN {
    // Pipeline Context
    private def pipeline

    // Tech
    private String name
    private String version
    private String artifactId
    private String nexusRepository
    private String url
    private def destination
    private String tech

    static final String type = 'jar'

    TechMVN(pipeline, name, version, artifactId, nexusRepository, url, destination) {
        this.pipeline = pipeline
        this.name = name
        this.version = version
        this.artifactId = artifactId
        this.nexusRepository = nexusRepository
        this.url = url
        this.destination = destination
    }

    def prepare() {
        // Download Code
        pipeline.checkout(scm: [$class: 'GitSCM', userRemoteConfigs: [[url: url, credentialsId: 'github-login-credentials']], branches: [[name: version]]],poll: false)

        // Set-up Maven
        def mvnHome = pipeline.tool name: 'Maven 3.9.4', type: 'maven'
        def mvnCmd = "${mvnHome}/bin/mvn"

        // Build
        pipeline.sh "${mvnCmd} clean package"

        def pomContent = readFile 'pom.xml'
        def groupId = pomContent =~ '<groupId>(.*?)</groupId>'
        def artifactId = pomContent =~ '<artifactId>(.*?)</artifactId>'
        def version = pomContent =~ '<version>(.*?)</version>'
        
        // Publicar el archivo JAR en GitHub Packages usando Maven
        pipeline.sh "${mvnCmd} deploy -Dmaven.deploy.skip=true -Dmaven.repo.local=\${WORKSPACE}/.m2/repository \
            -DaltDeploymentRepository=github::default::https://maven.pkg.github.com/R-dVL/cat-watcher \
            -DgroupId=${groupId[0][1]} -DartifactId=${artifactId[0][1]} -Dversion=${version[0][1]} \
            -Dfile=target/your-artifact.jar"
        // Upload artifact to Nexus
        //Nexus nexus = new Nexus(pipeline)
        //nexus.uploadArtifact(nexusRepository, version, artifactId, 'jar')
    }

    def deploy() {

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
