package edu.uade.patitas_peludas.controller;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.UserLoginDto;
import edu.uade.patitas_peludas.dto.UserRequestDTO;
import edu.uade.patitas_peludas.dto.UserResponseDTO;
import edu.uade.patitas_peludas.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    private IUserService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDTO<UserResponseDTO>> findAll(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String lastname,
                                                            @RequestParam(required = false) String dni,
                                                            @RequestParam(required = true) Short page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(name, lastname, dni, page));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Validated UserRequestDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Validated UserRequestDTO user) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, user));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDTO> updateState(@PathVariable Long id, @RequestParam boolean state) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateState(id, state));
    }

    @GetMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDTO> findByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByEmail(email));
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Validated UserLoginDto user) {
        return ResponseEntity.status(HttpStatus.OK).body(service.login(user));
    }
}
