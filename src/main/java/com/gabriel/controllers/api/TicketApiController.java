package com.gabriel.controllers.api;

import com.gabriel.dto.EventDTO;
import com.gabriel.dto.JoinEventRequest;
import com.gabriel.dto.TicketInfoQR;
import com.gabriel.dto.TicketQRRequest;
import com.gabriel.services.ticket.TicketService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketApiController {

    private final TicketService ticketService;

    public TicketApiController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * End-point que permite a un usuario unirse a un evento.
     *
     * @param joinEventRequest JoinEventRequest que contiene la información de la unión al evento.
     * @return true si la unión al evento es exitosa, false en caso contrario.
     */
    @PostMapping("/join")
    public boolean joinEvent(@RequestBody JoinEventRequest joinEventRequest) {
        return ticketService.joinEvent(joinEventRequest.username(), joinEventRequest.eventId());
    }
    /**
     * End-point que obtiene todos los eventos relacionados con los tickets del usuario actualmente autenticado.
     *
     * @param currentUser Detalles del usuario autenticado.
     * @return Lista de objetos EventDTO que representan los eventos relacionados con los tickets del usuario.
     */
    @GetMapping("/all-event-user")
    public List<EventDTO> getAllEventByTicket(@AuthenticationPrincipal UserDetails currentUser) {
        return ticketService.getEventsFromTicketsByUsername(currentUser.getUsername());
    }
    /**
     * End-point que verifica si un usuario ya se ha registrado para un evento específico.
     *
     * @param currentUser      Detalles del usuario autenticado.
     * @param joinEventRequest JoinEventRequest que contiene la información de la unión al evento.
     * @return true si el usuario ya se ha registrado para el evento, false en caso contrario.
     */
    @PostMapping("/alreadysigned")
    public boolean alreadySigned(@AuthenticationPrincipal UserDetails currentUser,
            @RequestBody JoinEventRequest joinEventRequest) {
        if (!currentUser.getUsername().equals(joinEventRequest.username())) {
            return false;
        }
        return ticketService.alreadySigned(joinEventRequest.username(), joinEventRequest.eventId());
    }
    /**
     * End-point que obtiene el número de tickets vendidos para un evento específico.
     *
     * @param id ID del evento.
     * @return Número de tickets vendidos para el evento.
     */
    @GetMapping("/countsold/{id}")
    public int getCountSold(@PathVariable Long id) {
        return ticketService.getTicketsSold(id);
    }

    /**
     * End-point que obtiene el código QR de un ticket para un usuario y evento específicos.
     *
     * @param ticketQRRequest TicketQRRequest que contiene la información del ticket y usuario.
     * @param currentUser     Detalles del usuario autenticado.
     * @param response        HttpServletResponse utilizado para establecer el estado de la respuesta.
     * @return TicketInfoQR que representa la información del ticket en formato de código QR.
     */
    @PostMapping()
    public TicketInfoQR getTicketQR(@RequestBody TicketQRRequest ticketQRRequest,
                                    @AuthenticationPrincipal UserDetails currentUser,
                                    HttpServletResponse response) {
        if (!currentUser.getUsername().equals(ticketQRRequest.username())) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return new TicketInfoQR("El usuario no coincide");
        }
        String ticketInfoQR = ticketService.getTicketByUserAndEvent(
                ticketQRRequest.username(), ticketQRRequest.event_id());
        if (ticketInfoQR == null) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        return ticketInfoQR != null ? new TicketInfoQR(ticketInfoQR) : new TicketInfoQR("El evento no esta disponible aun");
    }
}
