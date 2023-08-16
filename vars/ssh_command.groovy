package com.github.rdvl.automationLibrary

def call() {
    node {
        // Environment variables
        environment {
            cfg
        }
        // Pipeline error control
        try {
            // Configuration instance
            cfg = Configuration.getInstance()
            // Default Params
            Host host = new Host(this, HOST)

            stage('Host Setup') {
                // Retrieve info from Jenkins
                script {
                    // User & Password
                    withCredentials([
                        usernamePassword(credentialsId: host.getConfigCredentials(), usernameVariable: 'user', passwordVariable: 'password')]) {
                            host.setUser(user)
                            host.setPassword(password)
                    }
                    // IP
                    withCredentials([
                        string(credentialsId: host.getConfigIp(), variable: 'ip')]) {
                            host.setIp(ip)
                    }
                }
            }

            stage('Execute Command') {
                host.sshCommand(CMD)
            }

            stage('Send Telegram Notification') {
                String token
                String chatId
                String message

                withCredentials([
                    string(credentialsId: 'telegram-bot-token', variable: 'BOT_TOKEN')]) {
                        token = BOT_TOKEN
                }

                withCredentials([
                    string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')]) {
                        chatId = CHAT_ID
                }

                TelegramUtils tg = new TelegramUtils()
                tg.sendMessage(token, chatId, message)
            }

        } catch(Exception err) {
            println("ALERT | Something went wrong")
            error(err.getMessage())
        }
    }
}