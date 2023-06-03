package com.gabriel.services.event;

import com.gabriel.dto.EventDTO;
import com.gabriel.models.Event;
import com.gabriel.models.EventTableView;
import com.gabriel.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    boolean createEvent(Event event, User admin);
    boolean removeEvent(Long id, User user);
    boolean updateEvent(Event event, User user);
    Event getEventByIdAndAdmin(Long id, User user);
    Event getEventById(Long id);
    List<Event> getAllEvents(User user);
    List<EventTableView> getAllEventsTableView(User user);
    List<EventDTO> getAllAvailableEventsByUser(User user);
    EventDTO getEventDTO(Long Id);
    EventDTO eventParserDTO(Event event);
}
