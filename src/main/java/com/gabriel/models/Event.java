package com.gabriel.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event", unique = true, nullable = false)
    private Long id;
    @NotEmpty
    @Column(name = "name", length = 25)
    private String name;

    @NotEmpty
    @Column(name = "address", length = 50)
    private String address;

    @ManyToOne
    @JoinColumn(
            name = "organizer",
            foreignKey = @ForeignKey(name="USER_ID_FK"),
            nullable = false
    )
    private User organizer;

    @Min(value = 1)
    @Column(name = "max_assistants", length = 5)
    private Integer max_assistants;

    @NotEmpty
    @Column(name = "description", columnDefinition = "TEXT", length = 2000)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private LocalDateTime start_date;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finish_date")
    private LocalDateTime finish_date;

    @Min(value = 1)
    @Column(name = "min_age")
    @Check(constraints = "min_age >= 0")
    private Integer min_age;

    @OneToMany(mappedBy = "id_event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    // helpers
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setId_event(this);
    }
    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setId_event(null);
    }
}
