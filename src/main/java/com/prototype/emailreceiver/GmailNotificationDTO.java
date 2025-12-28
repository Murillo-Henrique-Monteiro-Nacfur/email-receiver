package com.prototype.emailreceiver;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class GmailNotificationDTO {
    public String emailAddress;
    public String historyId;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }
}
