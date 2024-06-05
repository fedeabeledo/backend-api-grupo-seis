package edu.uade.patitas_peludas.service;


import edu.uade.patitas_peludas.dto.UserLoginDto;
import edu.uade.patitas_peludas.dto.UserRequestDTO;
import edu.uade.patitas_peludas.dto.UserResponseDTO;
import edu.uade.patitas_peludas.dto.PageDTO;

public interface IUserService {
    PageDTO<UserResponseDTO> findAll(String name, String lastname, String dni, Short page);

    UserResponseDTO save(UserRequestDTO user);

    void deleteById(Long id);

    UserResponseDTO update(Long id, UserRequestDTO user);

    UserResponseDTO findByEmail(String email);

    UserResponseDTO login(UserLoginDto user);

    UserResponseDTO updateState(Long id, boolean state);
}
