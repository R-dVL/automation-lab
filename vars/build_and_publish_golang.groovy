package com.rdvl.jenkinsLibrary

def call() {
    node () {
        environment {
            project
            matrix
            configuration
            bin
        }
        ansiColor('xterm') {
            try {
                stage('Setup') {
                    cleanWs()    //Clean Workspace
                    configuration = readJSON(text: libraryResource(resource: 'configuration.json'))    // Read configuration file
                    matrix = ['windows', 'linux', 'darwin']    // OS Binaries to build
                    project = new Project(this, PROJECT_NAME, TAG)    // Init project
                    currentBuild.displayName = "${project.getName()}:${project.getVersion()}"    // Build name

                    // Clone project repository
                    // TODO: Use TAG env var to download selected version
                    checkout scmGit(
                        branches: [[name: "${project.getVersion()}"]],
                        userRemoteConfigs: [[url: "${project.getUrl()}"]]
                    )

                    // Binaries folder
                    sh("mkdir ${env.WORKSPACE}/bin")
                }

                stage('Build Compiler') {
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