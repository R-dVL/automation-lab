package com.github.rdvl.automationLibrary

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
import groovyx.net.http.RESTClient

class TelegramUtils {
    // Pipeline context
    private def pipeline

    TelegramUtils (pipeline) {
        this.pipeline = pipeline
    }

    @NonCPS
    def sendMessage(String token, String chatId, String message) {
        def apiUrl = "https://api.telegram.org/bot${token}/sendMessage"
        
        def restClient = new RESTClient(apiUrl)
        def response = restClient.post(
            contentType: 'application/json',
            body: [chat_id: chatId, text: message]
        )
        
        if (response.status == 200) {
            pipeline.println "Mensaje enviado exitosamente"
        } else {
            pipeline.println "Error al enviar el mensaje: ${response.status} - ${response.data}"
        }
    }
}