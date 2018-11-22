package com.zhgame.animalkindom.event.service;

import com.zhgame.animalkindom.event.entity.Event;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class EventService {

    public boolean hasUnReadEvent(Long animalId) {
        return eventRepository.getAllByReceiverAndReaded(animalId, false).size() > 0;
    }

    public List<Event> friendEvents(Long animalId) {
        return eventRepository.getAllByReceiverOrderByDateTimeDesc(animalId);
    }

    @Resource
    private EventRepository eventRepository;
}
