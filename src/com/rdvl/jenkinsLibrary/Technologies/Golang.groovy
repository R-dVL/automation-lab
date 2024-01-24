package com.rdvl.jenkinsLibrary
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
    private def parent

    /**
     * Constructor for the Golang technology class.
     *
     * @param parent Parent class.
     * @param steps The Jenkins pipeline steps context.
     * @param utils Utils object
     */
    Golang(parent) {
        this.parent = parent
        this.steps = parent.steps
        this.utils = parent.steps.utils
    }

    @NonCPS
    def build() {
        def parallelTech = [:]
        for (index in matrix) {
            def os = index
            parallelTech["${os}"] = {
                steps.sh("docker build --build-arg OS=${os} -t ${os}-builder .")    // Build compiler
                steps.sh("docker run --rm -v /DATA/AppData/jenkins/${env.WORKSPACE - /var/}/bin:/home/app/bin -e TAG=${TAG} ${os}-builder")    // Build binaries
            }
        }
        steps.parallel parallelTech
    }

    @NonCPS
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
                steps.archiveArtifacts artifacts: "bin/${parent.getArtifactName()}-${TAG}.${os}-amd64.${extension}"
            }
        }
        steps.parallel parallelTech
    }

    @NonCPS
    def deploy() {}
}
