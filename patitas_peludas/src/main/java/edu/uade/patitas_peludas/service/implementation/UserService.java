package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.*;
import edu.uade.patitas_peludas.entity.User;
import edu.uade.patitas_peludas.exception.UserExistsException;
import edu.uade.patitas_peludas.exception.UserNotActiveException;
import edu.uade.patitas_peludas.exception.UserNotFoundException;
import edu.uade.patitas_peludas.jwt.JwtUtils;
import edu.uade.patitas_peludas.repository.UserRepository;
import edu.uade.patitas_peludas.service.IUserService;
import edu.uade.patitas_peludas.service.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    private static final String USER_NOT_FOUND_ERROR_ID = "Could not find user with ID: %d.";
    private static final String USER_NOT_FOUND_ERROR_EMAIL = "Could not find user with email: %s.";
    private static final String USER_EXISTS_ERROR = "User with email %s already exists.";
    private static final String USER_NOT_ACTIVE_ERROR = "User with email %s is not active.";


    @Override
    public PageDTO<UserResponseDTO> findAll(String name, String lastname, String email, String sort, Short page) {
        Pageable pageable = buildPageable(sort, page);
        Specification<User> spec = buildSpec(name, lastname, email);

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        user.setPassword(String.valueOf(user.getPassword().hashCode()));
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
    public UserLoginResponseDTO login(UserLoginRequestDto user) {
        UserDetails userDetails = loadUserByUsername(user.getEmail());

        if (userDetails != null) {
            UserResponseDTO searchedUser = findByEmail(user.getEmail());

            if (!searchedUser.getState()) {
                throw new UserNotActiveException(String.format(USER_NOT_ACTIVE_ERROR, user.getEmail()));
            }

            String token = jwtUtils.generateToken((User) userDetails);
            return new UserLoginResponseDTO(token);
        }

        throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_EMAIL, user.getEmail()));
    }

    @Override
    public UserResponseDTO updateState(Long id, boolean state) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR_ID, id)));
        user.setState(state);
        User updated = repository.save(user);
        return mapper.convertValue(updated, UserResponseDTO.class);
    }

    private Pageable buildPageable(String sort, Short page) {
        List<String> sortCandidates = List.of("name", "lastname", "dni", "phone", "email", "role", "state");
        if (sort != null) {
            if (sortCandidates.contains(sort.toLowerCase())) {
                if (sort.equalsIgnoreCase("phone")) {
                    Sort sorted = Sort.by(Sort.Order.asc("phoneNumber"));
                    return PageRequest.of(page, 12, sorted);
                }
                Sort sorted = Sort.by(Sort.Order.asc(sort.toLowerCase()));
                return PageRequest.of(page, 12, sorted);
            }
        }
        Sort sorted = Sort.by(Sort.Order.asc("id"));
        return PageRequest.of(page, 12, sorted);
    }

    private Specification<User> buildSpec(String name, String lastname, String email) {
        Specification<User> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and(UserSpecification.nameSpec(name));
        }

        if (lastname != null) {
            spec = spec.and(UserSpecification.lastnameSpec(lastname));
        }

        if (email != null) {
            spec = spec.and(UserSpecification.emailSpec(email));
        }
        return spec;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> searchedUser = repository.findByEmail(username);
        if (searchedUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }
        return searchedUser.get();
    }
}
