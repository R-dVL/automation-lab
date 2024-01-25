package com.rdvl.jenkinsLibrary.technologies
/**
 * Represents Golang technology in the context of a Jenkins pipeline.
 */
public class Golang {
    // Pipeline Context
    private def steps
    private def utils

    // Tech params
    static final matrix = ['windows', 'linux', 'darwin']    // OS Binaries to build

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
    }

    def build() {
        def parallelTech = [:]
        for (index in matrix) {
            def os = index
            parallelTech["${os}"] = {
                steps.sh("docker build --build-arg OS=${os} -t ${os}-builder .")    // Build compiler
                steps.sh("docker run --rm -v /DATA/AppData/jenkins/${steps.env.WORKSPACE - /var/}/bin:/home/app/bin -e TAG=${project.getVersion()} ${os}-builder")    // Build binaries
            }
        }
        steps.parallel parallelTech
    }

    def publish() {
        def parallelTech = [:]
        for (index in matrix) {
            def os = index
            parallelTech["${os}"] = {
                String extension
                switch(os) {
                    case 'windows':
                        extension = 'exe'
                        break

                    case 'linux':
                        extension = 'bin'
                        break

                    case 'darwin':
                        extension = 'app'
                        break

                    default:
                        utils.log("OS: ${os}, not configured.", "red")
                        break
                }
                steps.archiveArtifacts artifacts: "bin/*.${extension}"
            }
        }
        steps.parallel parallelTech
    }

    def deploy() {}
}
