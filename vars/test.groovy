package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            try {
                stage('TEST') {
                    log = utils.log
                    text = 'lorem ipsum dolor'
                    log(text, 'black')
                    log(text, 'red')
                    log(text, 'green')
                    log(text, 'yellow')
                    log(text, 'blue')
                    log(text, 'purple')
                    log(text, 'cyan')
                    log(text, 'white')
                    log(text)
                    log(text, 'inventao')
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}