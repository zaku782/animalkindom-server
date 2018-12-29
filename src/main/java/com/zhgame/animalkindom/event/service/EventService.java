package com.zhgame.animalkindom.event.service;

import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.event.entity.FriendEvent;
import com.zhgame.animalkindom.tools.DateTool;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class EventService {

    public boolean hasUnReadEvent(Long animalId) {
        return friendEventRepository.getAllByReceiverAndReaded(animalId, false).size() > 0;
    }

    public List<FriendEvent> friendEvents(Long animalId) {
        return friendEventRepository.getAllByReceiverOrSenderOrderBySendTimeDesc(animalId, animalId);
    }

    public boolean haveRequested(Long sender, Long receiver) {
        List<FriendEvent> events = friendEventRepository.getByTypeAndSenderAndReceiverAndDone(FriendEvent.TYPE_FRIEND_REQUEST, sender, receiver, false);
        return events.size() > 0;
    }

    public void saveFriendEvent(FriendEvent event) {
        friendEventRepository.save(event);
    }

    public void friendAccept(Animal animal, String toWho) {

    }

    @Transactional
    public void friendReject(Animal animal, String toWho) {
        friendEventRepository.friendReject(FriendEvent.TYPE_FRIEND_REJECT, DateTool.getNowDateTime(), Long.parseLong(toWho), animal.getId(), FriendEvent.TYPE_FRIEND_REQUEST);
    }

    @Resource
    private FriendEventRepository friendEventRepository;
}
