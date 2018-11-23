package com.zhgame.animalkindom.event.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public class Event {

    private Long sender;
    private String senderName;
    private String senderSpecies;
    private Long receiver;
    private boolean readed;
    private LocalDateTime dateTime;
    private String type;
    private boolean done;

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderSpecies() {
        return senderSpecies;
    }

    public void setSenderSpecies(String senderSpecies) {
        this.senderSpecies = senderSpecies;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
