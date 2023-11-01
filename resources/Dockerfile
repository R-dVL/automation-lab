# Usa una imagen base de Ubuntu
FROM ubuntu:latest

# Actualiza el sistema y añade las dependencias
RUN apt-get update && \
    apt-get install -y ansible python3 python3-pip nodejs npm openssh-server

# Configura el servidor SSH
RUN mkdir /var/run/sshd
RUN sed -i 's/#PasswordAuthentication yes/PasswordAuthentication no/' /etc/ssh/sshd_config
RUN echo "AuthorizedKeysFile /root/.ssh/authorized_keys" >> /etc/ssh/sshd_config

# Añade tu clave pública SSH
COPY jenkins_agent_key.pub /root/.ssh/authorized_keys

# Expón el puerto 22 para SSH
EXPOSE 22

# Comando por defecto para iniciar el contenedor
CMD ["/usr/sbin/sshd", "-D"]
