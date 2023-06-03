package com.gabriel.models;

import com.gabriel.security.SecurityAuthority;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="User")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User implements Serializable, UserDetails {
    public enum TypeUser implements Serializable{
        USER, ADMIN, OPERATOR
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 25)
    private String name;

    @Column(name = "last_name_1", length = 50)
    private String lastName1;

    @Column(name = "last_name_2", length = 50)
    private String lastName2;

    @NotEmpty
    @Column(name = "username", length = 25, unique = true, nullable = false)
    private String username;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "DNI", unique = true, length = 9)
    private String dni;

    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type",columnDefinition = "ENUM('USER','ADMIN','OPERATOR')", nullable = false)
    private TypeUser typeUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime date_creation = LocalDateTime.now();

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "id_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminOperator> operatorRelations = new ArrayList<>();



    public User(String name,
                String lastName1,
                String lastName2,
                String username,
                String phone,
                String dni,
                String email,
                String password,
                TypeUser typeUser,
                LocalDateTime birthday) {
        this.name = name;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.username = username;
        this.phone = phone;
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.typeUser = typeUser;
        this.birthday = birthday;
    }
    /**
     * Constructor for User Type Operator
     * @param username username
     * @param password password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        typeUser = TypeUser.OPERATOR;
    }

    // helper methods
    public void addEvent(Event event) {
        events.add(event);
        event.setOrganizer(this);
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.setOrganizer(null);
    }

    // helper  tickets
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setId_user(this);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setId_user(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SecurityAuthority(typeUser));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
