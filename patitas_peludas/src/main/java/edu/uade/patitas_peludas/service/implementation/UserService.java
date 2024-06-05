package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.UserLoginDto;
import edu.uade.patitas_peludas.dto.UserRequestDTO;
import edu.uade.patitas_peludas.dto.UserResponseDTO;
import edu.uade.patitas_peludas.entity.User;
import edu.uade.patitas_peludas.exception.IncorrectPasswordException;
import edu.uade.patitas_peludas.exception.UserExistsException;
import edu.uade.patitas_peludas.exception.UserNotFoundException;
import edu.uade.patitas_peludas.repository.UserRepository;
import edu.uade.patitas_peludas.service.IUserService;
import edu.uade.patitas_peludas.service.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ObjectMapper mapper;

    private final String USER_NOT_FOUND_ERROR_ID = "Could not find user with ID: %d.";
    private final String USER_NOT_FOUND_ERROR_EMAIL = "Could not find user with email: %s.";
    private final String USER_EXISTS_ERROR = "User with email %s already exists.";
    private final String INCORRECT_PASSWORD_ERROR = "Incorrect password for user with email %s.";


    @Override
    public PageDTO<UserResponseDTO> findAll(String name, String lastname, String dni, Short page) {
        Pageable pageable = buildPageable(page);
        Specification<User> spec = buildSpec(name, lastname, dni);

        Page<User> res = repository.findAll(spec, pageable);

        List<UserResponseDTO> content = res.getContent().stream().map(user ->
                mapper.convertValue(user, UserResponseDTO.class)).collect(Collectors.toList());

        return new PageDTO<>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber(),
                res.getSize()
        );
    }

    @Override
    public UserResponseDTO save(UserRequestDTO user) {
        user.setPassword(String.valueOf(user.getPassword().hashCode()));
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserExistsException(String.format(USER_EXISTS_ERROR, user.getEmail()));
        }
        User entity = mapper.convertValue(user, User.class);
        User saved = repository.save(entity);

        return mapper.convertValue(saved, UserResponseDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_ID, id)));
        repository.deleteById(id);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO user) {
        if (repository.existsById(id)) {
            User mappedUser = mapper.convertValue(user, User.class);
            mappedUser.setId(id);
            User updated = repository.save(mappedUser);
            return mapper.convertValue(updated, UserResponseDTO.class);
        } else {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_ID, id));
        }
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_EMAIL, email));
        }
        return mapper.convertValue(user.get(), UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO login(UserLoginDto user) {
        Optional<User> searchedUser = repository.findByEmail(user.getEmail());
        if (searchedUser.isEmpty()) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_EMAIL, user.getEmail()));
        }
        if (!(searchedUser.get().getPassword().equals(String.valueOf(user.getPassword().hashCode())))) {
            throw new IncorrectPasswordException(String.format(INCORRECT_PASSWORD_ERROR, user.getEmail()));
        }
        return mapper.convertValue(searchedUser.get(), UserResponseDTO.class);
    }

    private Pageable buildPageable(Short page) {
        Sort sort = Sort.by(Sort.Order.asc("lastname"));
        return PageRequest.of(page, 12, sort);
    }

    private Specification<User> buildSpec(String name, String lastname, String dni) {
        Specification<User> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and(UserSpecification.nameSpec(name));
        }

        if (lastname != null) {
            spec = spec.and(UserSpecification.lastnameSpec(lastname));
        }

        if (dni != null) {
            spec = spec.and(UserSpecification.dniSpec(dni));
        }
        return spec;
    }
}
