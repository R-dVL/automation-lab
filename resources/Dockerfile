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
    apt-get install python3 nodejs npm && \
    apt-get install python3-pip && \
    apt-get install nodejs && \
    apt-get install npm && \
    apt-get install ansible

CMD ["bash"]