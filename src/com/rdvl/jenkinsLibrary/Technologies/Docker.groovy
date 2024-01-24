package com.rdvl.jenkinsLibrary.technologies
/**
 * Represents Golang technology in the context of a Jenkins pipeline.
 */
public class Docker {
    // Pipeline Context
    private def steps
    private def utils

    // Tech params
    private def image

    // Parent
    private def parent

    /**
     * Constructor for the Golang technology class.
     *
     * @param parent Parent class.
     * @param steps The Jenkins pipeline steps context.
     * @param utils Utils object
     */
    Docker(parent) {
        this.parent = parent
        this.steps = parent.steps
        this.utils = parent.steps.utils
    }

    @NonCPS
    def build() {
        this.image = steps.docker.build("ghcr.io/r-dvl/${parent.getArtifactName()}:${parent.getVersion()}")
    }

    @NonCPS
    def publish() {
        steps.docker.withRegistry('https://ghcr.io', 'github-user-password') {
            image.push()
        }
    }

    @NonCPS
    def deploy() {}
}
