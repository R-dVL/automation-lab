FROM jenkins/ssh-agent:debian-jdk17 as jenkins-agent

RUN apt-get update
RUN apt-get install -y ansible python3 python3-pip nodejs npm rsync

LABEL org.opencontainers.image.source https://github.com/r-dvl/jenkins-library

