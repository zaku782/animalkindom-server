package com.zhgame.animalkindom.event.entity;

import com.zhgame.animalkindom.tools.DateTool;

import javax.persistence.*;

@Entity
@Table(name = "friend_event")
public class FriendEvent extends Event {
    public static final String TYPE_FRIEND_REQUEST = "request";
    public static final String TYPE_FRIEND_REJECT = "reject";
    public static final String TYPE_FRIEND_ACCEPT = "accept";

    public FriendEvent() {

    }

    public FriendEvent(Long sender, String senderName, String senderSpecies, Long receiver, String type) {
        this.sender = sender;
        this.senderName = senderName;
        this.senderSpecies = senderSpecies;
        this.receiver = receiver;
        this.type = type;
        this.readed = false;
        this.done = false;
        this.sendTime = DateTool.getNowDateTime();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
