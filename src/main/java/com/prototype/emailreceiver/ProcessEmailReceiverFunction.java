package com.prototype.emailreceiver;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import io.quarkus.funqy.Funq;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessEmailReceiverFunction {

    private static final Logger LOGGER = Logger.getLogger(ProcessEmailReceiverFunction.class.getName());

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
    public void scheduledReport(PubSubMessage message) throws IOException {
        LOGGER.info("Scheduler trigger received. Message ID: "+ message.message.data);

        LOGGER.info("Scheduled task finished.");
    }
}
