# Usa una imagen de Linux como base
FROM debian:buster

# Instala Python, Node.js y npm
RUN apt-get update && \
    apt-get install -y python3 python3-pip nodejs npm && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    ln -s /usr/bin/pip3 /usr/bin/pip && \
    apt-get clean

# Instala Ansible
RUN pip install ansible

# Verifica las versiones instaladas
RUN python --version && \
    pip --version && \
    node --version && \
    npm --version && \
    ansible --version

# Agrega cualquier configuración o archivos necesarios para tu aplicación

# Define el directorio de trabajo (puedes cambiarlo según tus necesidades)
WORKDIR /app

# Comando predeterminado al iniciar el contenedor (puedes cambiarlo según tus necesidades)
CMD ["bash"]