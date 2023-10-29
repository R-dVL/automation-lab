FROM ubuntu:22.04

# Settings
ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000

RUN addgroup -g ${gid} ${group}
RUN adduser -h /home/${user} -u ${uid} -G ${group} -D ${user}
ARG AGENT_WORKDIR=/home/${user}/agent
USER ${user}
ENV AGENT_WORKDIR=${AGENT_WORKDIR}
RUN mkdir /home/${user}/.jenkins && mkdir -p ${AGENT_WORKDIR}
VOLUME /home/${user}/.jenkins
VOLUME ${AGENT_WORKDIR}
WORKDIR /home/${user}

# Dependencies
RUN apt-get update && \
    apt-get install -y python3 python3-pip nodejs npm && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    ln -s /usr/bin/pip3 /usr/bin/pip

# Install Ansible
RUN pip install ansible

WORKDIR /home/jenkins

CMD ["bash"]