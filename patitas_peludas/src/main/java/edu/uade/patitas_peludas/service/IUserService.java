package edu.uade.patitas_peludas.service;


import edu.uade.patitas_peludas.dto.*;

public interface IUserService {
    PageDTO<UserResponseDTO> findAll(String name, String lastname, String email, String sort, Short page);

    UserResponseDTO save(UserRequestDTO user);

    void deleteById(Long id);

    UserResponseDTO update(Long id, UserRequestDTO user);

    UserResponseDTO findByEmail(String email);

    UserLoginResponseDTO login(UserLoginRequestDto user);

    UserResponseDTO updateState(Long id, boolean state);
}
