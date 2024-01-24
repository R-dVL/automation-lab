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
    private Project project

    /**
     * Constructor for the Golang technology class.
     *
     * @param project Project class.
     * @param steps The Jenkins pipeline steps context.
     * @param utils Utils object
     */
    Docker(Project project) {
        this.project = project
        this.steps = project.steps
        this.utils = project.steps.utils
    }

    def build() {
        this.image = steps.docker.build("ghcr.io/r-dvl/${project.getArtifactName()}:${project.getVersion()}")
    }

    def publish() {
        steps.docker.withRegistry('https://ghcr.io', 'github-user-password') {
            image.push()
        }
    }

    def deploy() {}
}
