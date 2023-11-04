package com.rdvl.jenkinsLibrary


def call() {
    node ('server') {
        environment {
            configuration
            host
        }
        try {
            // Configuration instance
            String configurationJson = libraryResource resource: 'configuration.json'
            configuration = readJSON text: configurationJson

            // Default Params
            host = new Host(this, 'server')
            host.init()

            String command

            if (OPTION == 'ON') {
                command = '''
                    docker run -d --rm --name=jenkins-agent -p 4444:22 \
                    -e "JENKINS_AGENT_SSH_PUBKEY=ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDiOffAFudaYsHSK1qOPONbDN/GainjKjch0R7C5APp3kKk8yn04qChNus0OI+7vT+l22r477KMSh71NDfzDcIoQfRjtNnmAiMxXVrfiBVFUZDGh8CYq1gzSidpcfVsGbLbDoveSS2Egr+UEqiVw8jLAmSuaO2dxng5ZmqnXc3eT2R9+bhmOek45lWOwUKP1ose3v4+ueMjjZfVDsZTn9t0NzKXBBwdqA7hkPFubt6b5yWwNwo8gk4bwfG2DTSrLnUFucW+T1QGIEFlwgt1Bo4kwRCAsAvbFV3I0wIUGCD5bKqtHSOHWr/3y7MKIwr6O5HboyiDy1aL3LystxCmfWQzKpUsXmb/Db+o4alQoX0CM5vG6zXW9LSuIFNtMYTLRz+mjIF+mXhsNwFiV7Ni2zTkP0qVXWJknyV9n6fQBFPzlGIRchgdcr/0XQjGHAc38eb7I5k524JJ3QfmX43ymmhBCGoYKG7No3tyV5cZta/XjKvc/eUjmkWOID7aq7XnRoc= jenkins@rdvl-server" \
                    rdvlima/jenkins-agent
                '''
            } else if (OPTION == 'OFF') {
                command = 'docker stop jenkins-agent'
            }

            stage('Execute Command') {
                host.sshCommand(command)
            }

        } catch(Exception err) {
            error(err.getMessage())
        }
    }
}