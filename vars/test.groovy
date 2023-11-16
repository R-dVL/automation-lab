package com.rdvl.jenkinsLibrary

def call() {
    node ('docker-agent') {
        ansiColor('xterm') {
            try {
                stage('TEST') {
                    text = 'lorem ipsum dolor'
                    utils.log(text, 'black')
                    utils.log(text, 'red')
                    utils.log(text, 'green')
                    utils.log(text, 'yellow')
                    utils.log(text, 'blue')
                    utils.log(text, 'purple')
                    utils.log(text, 'cyan')
                    utils.log(text, 'white')
                    utils.log(text)
                    utils.log(text, 'inventao')
                }

            } catch(Exception e) {
                error(e.getMessage())
            }
        }
    }
}