package edu.uade.patitas_peludas.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDTO {
    private String token;
    private Long id;
}
