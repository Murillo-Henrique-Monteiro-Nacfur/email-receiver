package com.prototype.emailreceiver.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.History;
import com.google.api.services.gmail.model.ListHistoryResponse;
import com.google.api.services.gmail.model.Message;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.UserCredentials;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GmailHistoryService {

    @ConfigProperty(name = "oauth.client.id")
    String CLIENT_ID;

    @ConfigProperty(name = "oauth.client.secret")
    String CLIENT_SECRET;

    @ConfigProperty(name = "oauth.refresh.token")
    String REFRESH_TOKEN;

    public void processEmailByHistoryId(String historyIdFromPubSub) throws GeneralSecurityException, IOException {
        
        // 1. Setup Auth
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRefreshToken(REFRESH_TOKEN)
                .build();

        Gmail service = new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Gmail-History-Scanner")
                .build();

        // 2. O historyId vem como String do Pub/Sub, precisamos de BigInteger
        BigInteger startHistoryId = new BigInteger(historyIdFromPubSub);

        // 3. Listar o histórico a partir deste ID
        // O Gmail retornará tudo o que aconteceu DEPOIS desse ID
        ListHistoryResponse historyResponse = service.users().history()
                .list("me")
                .setStartHistoryId(startHistoryId)
                .setHistoryTypes(java.util.Arrays.asList("messageAdded")) // Focamos apenas em novos e-mails
                .execute();

        List<History> historyRecords = historyResponse.getHistory();

        if (historyRecords != null) {
            for (History history : historyRecords) {
                // Cada registro de histórico pode ter várias mensagens adicionadas
                if (history.getMessagesAdded() != null) {
                    for (com.google.api.services.gmail.model.HistoryMessageAdded messageAdded : history.getMessagesAdded()) {
                        
                        Message msgRef = messageAdded.getMessage();
                        
                        // 4. Agora buscamos o conteúdo completo da mensagem encontrada no histórico
                        Message fullMessage = service.users().messages()
                                .get("me", msgRef.getId())
                                .setFormat("full")
                                .execute();

                        System.out.println("Novo e-mail detectado!");
                        System.out.println("Assunto: " + getSubject(fullMessage));
                        System.out.println("Snippet: " + fullMessage.getSnippet());
                    }
                }
            }
        } else {
            System.out.println("Nenhuma alteração nova para este History ID.");
        }
    }

    // Método auxiliar para extrair o Assunto dos Headers
    private String getSubject(Message message) {
        return message.getPayload().getHeaders().stream()
                .filter(header -> header.getName().equalsIgnoreCase("Subject"))
                .map(header -> header.getValue())
                .findFirst()
                .orElse("Sem Assunto");
    }
}