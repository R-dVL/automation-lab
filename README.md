# Automation Library
_Shared library for training and device maintenance purposes._


## Table of Contents
1. [Project Structure](#Project%20Structure)
2. [Pipelines](#Pipelines)


## Project Structure
~~~text
(root)
+- resources                                # Resources (configuration files, dockerfile)
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
+- vars                                     # Pipelines
|   +- gallery_backup.groovy
|   +- ssh_command.groovy
|   +- deployment.groovy
|   +- ...
~~~

## Pipelines
### Gallery Backup
Performs a backup every 15 days, deleting old backups at the end.

### ssh Command
Sends a ssh command to any of the configured hosts in Jenkins.

### Deployment
Launch Ansible playbook to perform the deploy.
> [ansible-playbooks](https://github.com/R-dVL/ansible-playbooks.git)

