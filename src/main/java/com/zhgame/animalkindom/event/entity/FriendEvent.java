package com.zhgame.animalkindom.event.entity;

import javax.persistence.*;

@Entity
@Table(name = "friend_event")
public class FriendEvent extends Event {
    public static final String TYPE_FRIEND_REQUEST = "friend_request";
    public static final String TYPE_FRIEND_REJECT = "friend_reject";

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
