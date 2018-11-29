package com.zhgame.animalkindom.event.service;

import com.zhgame.animalkindom.event.entity.FriendEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class EventService {

    public boolean hasUnReadEvent(Long animalId) {
        return friendEventRepository.getAllByReceiverAndReaded(animalId, false).size() > 0;
    }

    public List<FriendEvent> friendEvents(Long animalId) {
        return friendEventRepository.getAllByReceiverOrderByDateTimeDesc(animalId);
    }

    public boolean haveRequested(Long sender, Long receiver) {
        List<FriendEvent> events = friendEventRepository.getByTypeAndSenderAndReceiverAndDone(FriendEvent.TYPE_FRIEND_REQUEST, sender, receiver, false);
        return events.size() > 0;
    }

    public void saveFriendEvent(FriendEvent event) {
        friendEventRepository.save(event);
    }

    @Resource
    private FriendEventRepository friendEventRepository;
}
