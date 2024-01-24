package com.rdvl.jenkinsLibrary

def call() {
    node () {
        environment {
            project
            matrix
            configuration
        }
        ansiColor('xterm') {
            try {
                stage('Prepare') {
                    cleanWs()    //Clean Workspace
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))    // Read configuration file
                    project = new Project(this, PROJECT_NAME, TAG)    // Init project

                    // Clone project repository
                    checkout scmGit(
                        branches: [[name: "${project.getVersion()}"]],
                        userRemoteConfigs: [[url: "${project.getUrl()}"]]
                    )

                    currentBuild.displayName = "${project.getName()}:${project.getVersion()}"    // Build name
                }

                stage('Build Compiler') {
                    matrix = ['windows', 'linux', 'darwin']    // OS Binaries to build
                    def parallelTech = [:]
                    for (index in matrix) {
                        def os = index
                        parallelTech["${os}"] = {
                            sh("docker build --build-arg OS=${os} -t ${os}-builder .")
                        }
                    }
                    parallel parallelTech
                }

                stage('Build Binaries') {
                    def parallelTech = [:]
                    for (index in matrix) {
                        def os = index
                        parallelTech["${os}"] = {
                            sh("docker run --rm -v /DATA/AppData/jenkins/${env.WORKSPACE - /var/}/bin:/home/app/bin -e TAG=${TAG} ${os}-builder")    // Using Server docker.sock
                        }
                    }
                    parallel parallelTech
                }

                stage('Upload Binaries') {
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
                            archiveArtifacts artifacts: "bin/${project.getArtifactName()}-${TAG}.${os}-amd64.${extension}"
                        }
                    }
                    parallel parallelTech
                }

            } catch(Exception err) {
                error(err.getMessage())
            }
        }
    }
}