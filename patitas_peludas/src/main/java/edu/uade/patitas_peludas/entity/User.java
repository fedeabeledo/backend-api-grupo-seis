package edu.uade.patitas_peludas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String name;

    @Column(nullable = false, length = 256)
    private String lastname;

    @Column(nullable = false, length = 256)
    private String dni;

    @Column(length = 256)
    private String phoneNumber;

    @Column(length = 256)
    private String email;

    @Column(length = 45)
    private String role;

    @Column(nullable = false)
    private Boolean state;

    // Getters and Setters
}
