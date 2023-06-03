package com.gabriel.controllers.api;

import com.gabriel.dto.VerifyRequest;
import com.gabriel.dto.VerifyResponse;
import com.gabriel.services.ticket.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operator")
public class OperatorApiController {
    private final TicketService ticketService;

    public OperatorApiController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    /**
     * End-point que verifica una entrada para permitir el acceso a un evento.
     *
     * @param verifyRequest VerifyRequest que contiene la información del ticket a verificar.
     * @return ResponseEntity con un objeto VerifyResponse en caso de éxito, o un ResponseEntity con un mensaje de error en caso contrario.
     */
    @PostMapping("/verify")
    public ResponseEntity<?> joinEvent(@RequestBody VerifyRequest verifyRequest) {

        VerifyResponse verifyResponse = ticketService.verifyTicket(verifyRequest);
        if (verifyResponse == null) {
            return ResponseEntity.badRequest().body("Ticket not valid");
        }
        return ResponseEntity.ok(verifyResponse);
    }
}
