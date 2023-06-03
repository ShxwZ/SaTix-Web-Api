package com.gabriel.services.event;

import com.gabriel.dto.EventDTO;
import com.gabriel.models.Event;
import com.gabriel.models.EventTableView;
import com.gabriel.models.User;
import com.gabriel.repositories.EventRepository;
import com.gabriel.repositories.TicketRepository;
import com.gabriel.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketService;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, TicketRepository ticketService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
    }
    /**
     * Crea un evento asociado al administrador especificado y lo guarda en el repositorio de eventos.
     * @return true si se crea correctamente, false en caso contrario.
     */
    @Override
    public boolean createEvent(Event event, User admin) {
        if (admin != null) {
            event.setOrganizer(admin);
            eventRepository.save(event);
            return true;
        }
        return false;
    }
    /**
     * Elimina un evento con el ID especificado si existe y el usuario tiene relación con el evento.
     * @return true si se elimina correctamente, false en caso contrario.
     */
    @Override
    public boolean removeEvent(Long id, User user) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null && userEventRelation(user, event)) {
            eventRepository.delete(event);
            return true;
        }
        return false;
    }
    /**
     * Actualiza un evento con la información proporcionada si existe y el usuario tiene relación con el evento.
     * @return true si se actualiza correctamente, false en caso contrario.
     */
    @Override
    public boolean updateEvent(Event event, User user) {
        User admin = userRepository.findByUsername(user.getUsername()).orElse(null);
        Event eventToUpdate = eventRepository.findById(event.getId()).orElse(null);
        if (eventToUpdate!= null && userEventRelation(admin, eventToUpdate)) {
            eventToUpdate.setName(event.getName());
            eventToUpdate.setDescription(event.getDescription());
            eventToUpdate.setAddress(event.getAddress());
            eventToUpdate.setStart_date(event.getStart_date());
            eventToUpdate.setFinish_date(event.getFinish_date());
            eventToUpdate.setMax_assistants(event.getMax_assistants());
            eventToUpdate.setMin_age(event.getMin_age());
            eventRepository.save(eventToUpdate);
            return true;
        }
        return false;
    }
    /**
     * Obtiene un evento por su ID y el administrador asociado, si existe y el usuario tiene relación con el evento.
     * @return el evento si se encuentra, de lo contrario retorna null.
     */
    @Override
    public Event getEventByIdAndAdmin(Long id, User user) {
        Event event = eventRepository.findById(id).orElse(null);
        if (userEventRelation(user, event))
            return event;
        else
            return null;
    }
    /**
     * Obtiene un evento por su ID.
     * @return el evento si se encuentra, de lo contrario retorna null.
     */
    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene todos los eventos asociados al usuario.
     * @return una lista de eventos.
     */
    @Override
    public List<Event> getAllEvents(User user) {
        return eventRepository.findAllEventByAdmin(user)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene todos los eventos asociados al usuario en formato de tabla de eventos.
     * @return una lista de EventTableView.
     */
    @Override
    public List<EventTableView> getAllEventsTableView(User user) {
        return getAllEvents(user).stream()
                        .map(EventTableView::new)
                        .collect(Collectors.toList());
    }
    /**
     * Obtiene todos los eventos disponibles para el usuario.
     * @return una lista de EventDTO.
     */
    @Override
    public List<EventDTO> getAllAvailableEventsByUser(User user) {
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findAll().forEach(event -> {
            if(
                    event.getFinish_date().isAfter(LocalDateTime.now()) //&&
                    //ticketService.findTicketByEventAndUser(event,user) == null
            ){
                events.add(eventParserDTO(event));
            }
        });
        return events;
    }
    /**
     * Obtiene un EventDTO para el evento con el ID especificado.
     * @return EventDTO si se encuentra el evento, de lo contrario retorna null.
     */
    @Override
    public EventDTO getEventDTO(Long Id) {
        Event event = eventRepository.findById(Id).orElse(null);
        return event != null ? eventParserDTO(event) : null;
    }

    /**
     * Verifica si el usuario tiene una relación con el evento.
     * @return true si hay relación, false en caso contrario.
     */
    public boolean userEventRelation(User user, Event event) {
        return event != null && event.getOrganizer().equals(user);
    }
    /**
     * Convierte un Event en un EventDTO.
     * @return un EventDTO correspondiente al evento dado.
     */
    public EventDTO eventParserDTO(Event event){
        int ticketsSold = ticketService.countTicketById_event(event.getId());
        return new EventDTO(
                event.getId(),
                event.getAddress(),
                event.getDescription(),
                event.getFinish_date(),
                event.getMax_assistants(),
                ticketsSold,
                event.getMin_age(),
                event.getName(),
                event.getStart_date(),
                event.getOrganizer().getId()
        );
    }


}
