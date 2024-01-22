FROM jenkins/jenkins:lts-alpine

# Install dependencies
USER root

RUN apk update && \
    apk add ansible python3 nodejs npm rsync sshpass && \
    rm -rf /var/cache/apk/*

USER jenkins

LABEL org.opencontainers.image.source https://github.com/r-dvl/jenkins-library