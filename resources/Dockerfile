FROM ubuntu:22.04

# Install Python, Node.js y npm
RUN apt-get update && \
    apt-get install -y python3 python3-pip nodejs npm && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    ln -s /usr/bin/pip3 /usr/bin/pip

# Install Ansible
RUN pip install ansible

WORKDIR /home/jenkins

CMD ["bash"]