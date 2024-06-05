package edu.uade.patitas_peludas.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "User")
public class User {

    // Getters y Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "lastname", nullable = false, length = 256)
    private String lastname;

    @Column(name = "dni", nullable = false, length = 256)
    private String dni;

    @Column(name = "phone_number", length = 256)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "role", length = 45)
    private String role;

    @Column(name = "state", nullable = false)
    private Boolean state;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
