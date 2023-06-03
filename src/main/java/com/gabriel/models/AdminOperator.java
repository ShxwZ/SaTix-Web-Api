package com.gabriel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_operator_relation")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AdminOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = false)
    private User operator;

    @Column(name = "password_no_encode")
    private String passwordNoEncode;


    // Constructor, getters y setters

    public AdminOperator(User admin, User operator, String passwordNoEncode) {
        this.admin = admin;
        this.operator = operator;
        this.passwordNoEncode = passwordNoEncode;
    }
}
