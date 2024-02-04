package com.rdvl.jenkinsLibrary.technologies
/**
 * Represents Golang technology in the context of a Jenkins pipeline.
 */
public class Golang {
    // Pipeline Context
    private def steps
    private def utils
    private def platforms

    // Parent
    private def project

    /**
     * Constructor for the Golang technology class.
     *
     * @param project Project class.
     * @param steps The Jenkins pipeline steps context.
     * @param utils Utils object
     */
    Golang(project) {
        this.project = project
        this.steps = project.steps
        this.utils = project.steps.utils
        this.platforms = steps.configuration.cicd.projects."${project.getName()}".platforms
    }

    def build() {
        def parallelTech = [:]
        platforms.each { os, archs ->
            archs.each { arch ->
                parallelTech["${os}-${arch}"] = {
                    steps.sh("docker build --build-arg OS=${os} --build-arg ARCH=${arch} -t ${os}-${arch}-builder .")    // Build compiler
                    steps.sh("docker run --rm -v ${steps.env.WORKSPACE}/bin:/home/app/bin -e VERSION=${project.getVersion()} ${os}-${arch}-builder")    // Build binaries
                }
            }
        }
        steps.parallel parallelTech
    }

    def publish() {
        def parallelTech = [:]
        platforms.each { os, archs ->
            archs.each { arch ->
                parallelTech["${os}-${arch}"] = {
                    String fileName = "${project.getName()}-${project.getVersion()}.${os}-${arch}"
                    steps.zip zipFile: "${fileName}.zip", dir: "bin/${fileName}"
                    steps.archiveArtifacts artifacts: "${fileName}.zip", onlyIfSuccessful: true, fingerprint: true
                }
            }
        }
        steps.parallel parallelTech
    }

    def deploy() {}
}
