package com.gabriel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User id_user;

    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    private Event id_event;

    @Column(name = "code_qr")
    private String code_qr;
    public Ticket(User id_user, Event id_event, String code_qr) {
        this.id_user = id_user;
        this.id_event = id_event;
        this.code_qr = code_qr;
    }
}
