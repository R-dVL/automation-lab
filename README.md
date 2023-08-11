# automationLibrary
Shared library for training and device maintenance purposes

## Project structure
~~~text
(root)
+- resources                                # Resources (configuration files, playbooks)
|   +- configuration.json
|
+- src                                      # Groovy source files (Classes)
|   +- io
|       +- rdvl
|           +- automationLibrary
|               +- Host.groovy
|               +- Constants.groovy
|
+- vars                                     # Pipelines
|   +- gallery_backup.groovy
|   +- ssh_command.groovy
~~~

## Pipelines
### Gallery Backup
Performs a backup every 15 days, deleting old backups at the end.

### ssh Command
Sends a ssh command to any of the configured hosts in Jenkins.

## Classes
### Constants
Constants singleton to store urls and paths.

### Host
Host class with Jenkins functions wrapped.

## Resources
### Configuration
Configuration JSON with parameters such as hosts.
