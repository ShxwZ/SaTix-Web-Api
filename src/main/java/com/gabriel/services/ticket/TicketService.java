package com.gabriel.services.ticket;

import com.gabriel.dto.EventDTO;
import com.gabriel.dto.VerifyRequest;
import com.gabriel.dto.VerifyResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketService {
    boolean joinEvent(String username, Long eventId);
    int getTicketsSold(Long eventId);
    boolean alreadySigned(String username, Long eventId);
    VerifyResponse verifyTicket(VerifyRequest verifyRequest);
    String getTicketByUserAndEvent(String username, Long eventId);
    List<EventDTO> getEventsFromTicketsByUsername(String username);
}
