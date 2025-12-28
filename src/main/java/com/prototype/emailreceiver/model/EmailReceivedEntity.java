package com.prototype.emailreceiver.model;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "email-received", schema = "public")
public class EmailReceivedEntity extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "emailReceivedSequence",
            sequenceName = "email_received_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailReceivedSequence")
    public Long id;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false, length = 255)
    private String orignalDestination;
    
    @Column(nullable = false, length = 255)
    private String orignalSender;

    @Column(nullable = false, length = 255)
    private String status;

    @Column(name = "date_hour_creation")
    private LocalDateTime dateHourCriation;

    @Column(name = "date_hour_received")
    private LocalDateTime dateHourReceived;

    public EmailReceivedEntity() {
    }

    public EmailReceivedEntity(String body, String orignalDestination, String orignalSender, String status, LocalDateTime dateHourCriation, LocalDateTime dateHourReceived) {
        this.body = body;
        this.orignalDestination = orignalDestination;
        this.orignalSender = orignalSender;
        this.status = status;
        this.dateHourCriation = dateHourCriation;
        this.dateHourReceived = dateHourReceived;
    }
    public Long getId() {
        return id;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getOrignalDestination() {
        return orignalDestination;
    }
    public void setOrignalDestination(String orignalDestination) {
        this.orignalDestination = orignalDestination;   
    }
    public String getOrignalSender() {
        return orignalSender;
    }
    public void setOrignalSender(String orignalSender) {
        this.orignalSender = orignalSender;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getDateHourCriation() {
        return dateHourCriation;
    }
    public void setDateHourCriation(LocalDateTime dateHourCriation) {
        this.dateHourCriation = dateHourCriation;
    }
    public LocalDateTime getDateHourReceived() {
        return dateHourReceived;
    }
    public void setDateHourReceived(LocalDateTime dateHourReceived) {
        this.dateHourReceived = dateHourReceived;
    }
    
}

