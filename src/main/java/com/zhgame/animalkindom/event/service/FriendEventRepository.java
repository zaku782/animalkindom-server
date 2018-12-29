package com.zhgame.animalkindom.event.service;

import com.zhgame.animalkindom.event.entity.FriendEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FriendEventRepository extends JpaRepository<FriendEvent, Long> {
    List<FriendEvent> getAllByReceiverAndReaded(Long receiver, boolean readed);

    List<FriendEvent> getAllByReceiverOrSenderOrderBySendTimeDesc(Long receiver, Long sender);

    List<FriendEvent> getByTypeAndSenderAndReceiverAndDone(String type, Long sender, Long receiver, boolean done);

    @Modifying
    @Query("update FriendEvent e set e.type = ?1,e.done=1,e.responseTime=?2 where e.sender = ?3 and e.receiver=?4 and e.type=?5")
    void friendReject(String toType, LocalDateTime now,Long sender,Long toWho,String fromType);
}
