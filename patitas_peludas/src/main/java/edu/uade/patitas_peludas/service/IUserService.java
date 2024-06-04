package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.UserDTO;

public interface IUserService {
    PageDTO<UserDTO> findAll(String name, String lastname, String dni, Short page);

    UserDTO save(UserDTO user);

    void deleteById(Long id);

    UserDTO update(Long id, UserDTO user);

    UserDTO findByEmail(String email);
}
