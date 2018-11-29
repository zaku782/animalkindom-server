package com.zhgame.animalkindom.event.service;

import com.zhgame.animalkindom.event.entity.FriendEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FriendEventRepository extends JpaRepository<FriendEvent, Long> {
    List<FriendEvent> getAllByReceiverAndReaded(Long receiver, boolean readed);

    List<FriendEvent> getAllByReceiverOrderByDateTimeDesc(Long receiver);

    List<FriendEvent> getByTypeAndSenderAndReceiverAndDone(String type, Long sender, Long receiver, boolean done);
}
