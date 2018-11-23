package com.zhgame.animalkindom.event.service;

import com.zhgame.animalkindom.event.entity.Event;
import com.zhgame.animalkindom.event.entity.FriendEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class EventService {

    public boolean hasUnReadEvent(Long animalId) {
        return friendEventRepository.getAllByReceiverAndReaded(animalId, false).size() > 0;
    }

    public List<Event> friendEvents(Long animalId) {
        return friendEventRepository.getAllByReceiverOrderByDateTimeDesc(animalId);
    }

    public void saveFriendEvent(FriendEvent event) {
        friendEventRepository.save(event);
    }

    @Resource
    private FriendEventRepository friendEventRepository;
}
