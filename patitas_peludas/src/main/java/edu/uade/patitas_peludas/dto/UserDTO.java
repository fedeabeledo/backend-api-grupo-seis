package edu.uade.patitas_peludas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private Long id;

    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String lastname;

    @NotBlank
    @Size(max = 256)
    private String dni;

    @Size(max = 256)
    private String phoneNumber;

    @Email
    @Size(max = 256)
    private String email;

    @Size(max = 45)
    private String role;

    private Boolean state;

}
