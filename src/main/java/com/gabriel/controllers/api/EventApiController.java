package com.gabriel.controllers.api;

import com.gabriel.dto.EventDTO;
import com.gabriel.models.User;
import com.gabriel.services.event.EventService;
import com.gabriel.services.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/event")
public class EventApiController {
    private final EventService eventService;
    private final UserService userService;

    public EventApiController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }
    /**
     * End-point que obtiene todos los eventos disponibles para el usuario autenticado.
     *
     * @param currentUser Detalles del usuario autenticado.
     * @return Lista de EventDTO que representan los eventos disponibles para el usuario, o null si el usuario no existe.
     */
    @GetMapping("")
    public List<EventDTO> getAllEvents(@AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.getUserByUsername(currentUser.getUsername());
        if (user == null) return null;
        return eventService.getAllAvailableEventsByUser(user);
    }
    /**
     * End-point que obtiene un evento específico por su ID.
     *
     * @param id       ID del evento a obtener.
     * @param response HttpServletResponse utilizado para establecer el estado de la respuesta.
     * @return EventDTO que representa el evento solicitado, o null si no se encuentra el evento.
     */
    @GetMapping("/{id}")
    public EventDTO getEventByID(@PathVariable Long id,
                                 HttpServletResponse response) {
        EventDTO eventDTO = eventService.getEventDTO(id);
        if (eventDTO == null) response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return eventDTO;
    }


}
