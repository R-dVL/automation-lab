# Automation Library
_Shared library for training and device maintenance purposes._


## Table of Contents
1. [Project Structure](#Project%20Structure)
2. [Pipelines](#Pipelines)


## Project Structure
~~~text
(root)
+- resources                                # Resources (configuration files, Dockerfiles..)
|   +- configuration.json
|   +- Dockerfile
|   +- keys
|       +- ssh_keys.pub
|   +- ...
+- src                                      # Groovy source files (Classes)
|   +- com
|       +- rdvl
|           +- jenkinsLibrary
|               +- Host.groovy
|               +- Project.groovy
|               +- ...
+- vars                                     # Pipelines
|   +- gallery_backup.groovy
|   +- ssh_command.groovy
|   +- deployment.groovy
|   +- ...
~~~

## Pipelines
### Gallery Backup
Performs a backup of my stored photos every 15 days, deleting old backups at the end.


### SSH Command
Sends a SSH command to any of the configured hosts in Jenkins.
> Used mainly to turn On/Off services with Jenkins Cron configuration.


### Deployment
Launch [ansible-playbooks](https://github.com/R-dVL/ansible-playbooks.git) to perform the deploy.


### Create Docker Image
It reads the Dockerfile repository from the resources, builds the image and push it to Dockerhub.


### Docker Agent
Starts or stops the Docker Agent Container in server using the _principal Node_.
> [Create Jenkins Agent](https://gist.github.com/R-dVL/374d1e0bd23f4d1f52dcb48f1d27f4b7)


### Cat Watcher Dataset
Fetchs photos taken with the [cat-watcher](https://github.com/R-dVL/cat-watcher.git) application separating cats from non-cats to augment the data in the AI model training dataset.


## Utils
---
Jenkins scripts written in vars are instantiated on-demand as singletons. Auxiliary functions and Jenkins wrappers are defined here to being used in pipelines and classes such as _Host.groovy_.

