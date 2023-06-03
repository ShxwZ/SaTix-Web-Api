package com.gabriel.repositories;

import com.gabriel.models.Event;
import com.gabriel.models.Ticket;
import com.gabriel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.id_event.id = ?1")
    int countTicketById_event(Long id_event);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.id_event = ?1 AND t.id_user = ?2")
    int userHasTicket(Event id_event, User user_id);

    @Query("SELECT t FROM Ticket t WHERE t.id_event = ?1 AND t.id_user = ?2")
    Ticket findTicketByEventAndUser(Event event, User user);
    @Query("SELECT t.id_event FROM Ticket t WHERE t.id_user = ?1")
    List<Event> getEventsFromTicketsByUsername(User user);
}
