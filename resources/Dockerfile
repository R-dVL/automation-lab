FROM jenkins/jenkins:lts

# Install Docker dependencies
USER root

RUN apt-get update -qq \
    && apt-get install -qqy apt-transport-https ca-certificates curl gnupg2 software-properties-common

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -

RUN add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian \
   $(lsb_release -cs) \
   stable"

RUN apt-get update -qq \
    && apt-get -y install docker-ce

RUN usermod -aG docker jenkins

# Install Ansible
RUN apt-get update -y && apt-get install -y \
    ansible \
    sshpass

# Repository connection
LABEL org.opencontainers.image.source https://github.com/r-dvl/jenkins-library