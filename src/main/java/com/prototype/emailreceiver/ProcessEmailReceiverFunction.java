package com.prototype.emailreceiver;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prototype.emailreceiver.service.GmailHistoryService;
import io.quarkus.funqy.Funq;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessEmailReceiverFunction {

    private static final Logger LOGGER = Logger.getLogger(ProcessEmailReceiverFunction.class.getName());
    private final GmailHistoryService gmailHistoryService;

    public ProcessEmailReceiverFunction(GmailHistoryService gmailHistoryService) {
        this.gmailHistoryService = gmailHistoryService;
    }

    public static class PubSubMessage {
        public Message message;
        public String subscription;
    }

    public static class Message {
        public String data;
        public Map<String, String> attributes;
        public String messageId;
    }

    @Funq("emailReceiverProcessor")
    public void scheduledReport(PubSubMessage message) throws Exception {
        LOGGER.info("Scheduler trigger received. Message ID: "+ message.message.data);
        GmailNotificationDTO gmailNotificationDTO = parseNotification(message);
        gmailHistoryService.processEmailByHistoryId(gmailNotificationDTO.getHistoryId());
        LOGGER.info("Scheduled task finished.");
    }

    public GmailNotificationDTO parseNotification(PubSubMessage message) throws Exception {
        // Passo 1: Converter o JSON bruto para o envelope do PubSub
        ObjectMapper objectMapper = new ObjectMapper();
        // Passo 2: Pegar a string Base64 do campo 'data'
        String base64Data = message.message.data;

        // Passo 3: Decodificar o Base64 para a String JSON original do Gmail
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String decodedJson = new String(decodedBytes);

        // Passo 4: Converter o JSON decodificado para o DTO final do Gmail
        return objectMapper.readValue(decodedJson, GmailNotificationDTO.class);
    }
}
