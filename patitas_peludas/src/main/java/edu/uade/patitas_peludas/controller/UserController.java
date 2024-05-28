package edu.uade.patitas_peludas.controller;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.UserDTO;
import edu.uade.patitas_peludas.service.IUserService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    private IUserService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDTO<UserDTO>> findAll(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String lastname,
                                                    @RequestParam(required = false) String dni,
                                                    @RequestParam(required = false) @Pattern(regexp = "asc|desc") String sort,
                                                    @RequestParam(required = true) Short page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(name, lastname, dni, page));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> save(@RequestBody @Validated UserDTO user) {
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
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Validated UserDTO user) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, user));
    }
}
