# automationLibrary
Shared library for training and device maintenance purposes

## Project structure
~~~text
(root)
+- resources                                # Resources (configuration files, playbooks)
|   +- configuration.json
|   +- ...
+- src                                      # Groovy source files (Classes)
|   +- com
|       +- rdvl
|           +- automationLibrary
|               +- Host.groovy
|               +- Configuration.groovy
|               +- Project.groovy
|               +- TechMVN.groovy
|               +- TechNPM.groovy
|               +- TechPY.groovy
|               +- ...
+- vars                                     # Pipelines
|   +- gallery_backup.groovy
|   +- ssh_command.groovy
|   +- deployment.groovy
|   +- ...
~~~

## Pipelines
- Gallery Backup
    - Performs a backup every 15 days, deleting old backups at the end.

- ssh Command
    - Sends a ssh command to any of the configured hosts in Jenkins.

- Deployment
    - Build and upload artifacts of my projects and deploy the new version to the server/rpi.

## Classes
- Constants
    - Constants singleton to store urls and paths.

- Host
    - Host class with Jenkins functions wrapped.

- Configuration
    - Singleton with configuration (reading configuration.json with JsonSlurper gave me problems and figures as a bad practice)
    > [Jenkins Best Practices](https://www.jenkins.io/doc/book/pipeline/pipeline-best-practices/)

- Project
    - Gather project parameters and init the Tech that uses for the deployment

- TechMVN
    - Maven technology

- TechNPM
    - Node.js technology

- TechPY
    - Python technology
