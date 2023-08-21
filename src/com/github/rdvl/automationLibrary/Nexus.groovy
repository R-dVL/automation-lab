package com.github.rdvl.automationLibrary

class Nexus implements Serializable {
    // Pipeline Context
    private def pipeline

    // Constants
    static final String nexusVersion = 'nexus3'
    static final String protocol = 'http'
    static final String nexusUrl = '192.168.1.55:8081'
    static final String groupId = 'com.rdvl'
    static final String credentialsId = 'nexus-credentials'

    Nexus(pipeline) {
        // Pipeline context setup
        this.pipeline = pipeline
    }

    @NonCPS
    def uploadArtifact(repository, version, artifactId, type) {
        pipeline.nexusArtifactUploader(
            nexusVersion: nexusVersion,
            protocol: protocol,
            nexusUrl: nexusUrl,
            groupId: groupId,
            version: version,
            repository: repository,
            credentialsId: credentialsId,
            artifacts: [
                [artifactId: artifactId,
                classifier: '',
                file: 'target/' + artifactId + '-' + version + '.' + type,
                type: type]
            ]
        );
    }


    @Override
    @NonCPS
    public String toString() {
        return """
            Name: ${name}
            Config IP: ${configIp}
            Credentials: ${configCredentials}
            User: ${user}
            Password: ${password}
            IP: ${ip}
        """
    }
}