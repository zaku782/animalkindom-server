package com.zhgame.animalkindom.event.service;

import com.zhgame.animalkindom.event.entity.Event;
import com.zhgame.animalkindom.event.entity.FriendEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FriendEventRepository extends JpaRepository<FriendEvent, Long> {

    List<Event> getAllByReceiverAndReaded(Long receiver, boolean readed);

    List<Event> getAllByReceiverOrderByDateTimeDesc(Long receiver);
}
