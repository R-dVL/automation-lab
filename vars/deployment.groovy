package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params

            stage('Build') {
                cleanWs()
                Project prj = new Project(this, NAME, VERSION)
                print(prj)

                prj.downloadCode()

                def mvnHome = tool name: 'Maven 3.9.4', type: 'maven'
                def mvnCmd = "${mvnHome}/bin/mvn"

                sh "${mvnCmd} clean package"
            }

            stage('Publish') {
                withCredentials([
                    usernamePassword(credentialsId: 'github-token', usernameVariable: 'user', passwordVariable: 'password')]) {

                    def settingsXml = """
                    <settings>
                        <servers>
                            <server>
                                <id>github</id>
                                <username>${user}</username>
                                <password>${password}</password>
                            </server>
                        </servers>
                    </settings>
                    """
                    writeFile file: "${env.WORKSPACE}/.m2/settings.xml", text: settingsXml
                }
                def mvnHome = tool name: 'Maven 3.9.4', type: 'maven'
                def mvnCmd = "${mvnHome}/bin/mvn"

                sh "${mvnCmd} deploy --settings ${env.WORKSPACE}/.m2/settings.xml"
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}