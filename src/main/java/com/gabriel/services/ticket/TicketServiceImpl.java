package com.gabriel.services.ticket;

import com.gabriel.dto.*;
import com.gabriel.models.Event;
import com.gabriel.models.Ticket;
import com.gabriel.models.User;
import com.gabriel.repositories.TicketRepository;
import com.gabriel.security.topt.TicketCodeGenerator;
import com.gabriel.services.event.EventService;
import com.gabriel.services.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventService eventService;
    private final UserService userService;
    private final TicketCodeGenerator ticketCodeGenerator;
    @Value("${app.qr.url.api}")
    private String QR_API_URL;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             EventService eventService,
                             UserService userService,
                             TicketCodeGenerator ticketCodeGenerator) {
        this.ticketRepository = ticketRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.ticketCodeGenerator = ticketCodeGenerator;
    }
    /**
     * Registra a un usuario en un evento / crea un ticket.
     *
     * @param username nombre de usuario que desea unirse al evento.
     * @param eventId  ID del evento al que se desea unir.
     * @return true si se realiza correctamente, false en caso contrario.
     */
    @Override
    public boolean joinEvent(String username, Long eventId) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByUsername(username);
        if (event == null || user == null)
            return false;
        if (alreadySigned(username, eventId))
            return false;
        int totalTickets = ticketRepository.countTicketById_event(event.getId());
        if (totalTickets >= event.getMax_assistants())
            return false;

        String tokenTicket = ticketCodeGenerator.generateSecretKey();
        Ticket ticket = new Ticket(user,event,tokenTicket);

        ticketRepository.save(ticket);

        return true;
    }
    /**
     * Obtiene el número de entradas de un evento específico.
     *
     * @param eventId ID del evento.
     * @return el número de entradas.
     */
    @Override
    public int getTicketsSold(Long eventId) {
        return ticketRepository.countTicketById_event(eventId);
    }
    /**
     * Verifica si un usuario ya se ha registrado en un evento específico.
     *
     * @param username nombre de usuario.
     * @param eventId  ID del evento.
     * @return true si el usuario ya está registrado, false en caso contrario.
     */
    @Override
    public boolean alreadySigned(String username, Long eventId) {
        User user = userService.getUserByUsername(username);
        Event event = eventService.getEventById(eventId);
        if (user == null)
            return false;
        return ticketRepository.userHasTicket(event, user) != 0;
    }
    /**
     * Verifica la validez de una entrada.
     *
     * @param verifyRequest VerifyRequest que contiene la información necesaria para la verificación.
     * @return un VerifyResponse si el entrada es válido, null en caso contrario.
     */
    @Override
    public VerifyResponse verifyTicket(VerifyRequest verifyRequest) {
        Event event = eventService.getEventById(Long.valueOf(verifyRequest.eventId()));
        User user = userService.getUserById(Long.valueOf(verifyRequest.userId()));
        if (event == null || user == null)
            return null;
        Ticket ticket = ticketRepository.findTicketByEventAndUser(event, user);
        if (ticket == null)
            return null;
        TicketCodeGenerator ticketCodeGenerator = new TicketCodeGenerator();
        boolean validateTicket = ticketCodeGenerator.validateTOTP(ticket.getCode_qr(), Integer.parseInt(verifyRequest.code()));
        return validateTicket ?
                new VerifyResponse(
                        user.getName(),
                        user.getLastName1(),
                        user.getLastName2(),
                        user.getPhone(),
                        user.getDni(),
                        event.getName()) : null;
    }
    /**
     * Obtiene la entrada de un usuario para un evento específico en formato de código QR.
     *
     * @param username nombre de usuario del usuario.
     * @param eventId  ID del evento.
     * @return el código QR de la entrada si es válido y está dentro del período del evento, null en caso contrario.
     */
    @Override
    public String getTicketByUserAndEvent(String username, Long eventId) {
        User user = userService.getUserByUsername(username);
        Event event = eventService.getEventById(eventId);
        if (user == null || event == null)
            return "Invalid user or event";
        Ticket ticket = ticketRepository.findTicketByEventAndUser(event, user);
        if (ticket == null)
            return "User has no ticket for this event";
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(event.getStart_date()) && now.isBefore(event.getFinish_date())) {
            int code = ticketCodeGenerator.generateTOTP(ticket.getCode_qr());
            return String.format("%s%d;%s;%s", QR_API_URL,eventId, code,user.getId());
        }
        return null;
    }
    /**
     * Obtiene una lista de eventos asociados a un usuario específico a partir de las entradas registradas.
     *
     * @param username nombre de usuario.
     * @return una lista de EventDTO.
     */
    @Override
    public List<EventDTO> getEventsFromTicketsByUsername(String username) {
        User user = userService.getUserByUsername(username);
        if (user == null)
            return null;
        List<Event> events =ticketRepository.getEventsFromTicketsByUsername(user);
        if (events == null)
            return null;

        return events.stream().map(eventService::eventParserDTO).toList();
    }
}
