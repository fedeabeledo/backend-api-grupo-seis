package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.UserDTO;
import edu.uade.patitas_peludas.entity.User;
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
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ObjectMapper mapper;

    String USER_NOT_FOUND_ERROR = "Could not find user with ID: %d.";

    @Override
    public PageDTO<UserDTO> findAll(String name, String lastname, String dni, Short page) {
        Pageable pageable = buildPageable(page);
        Specification<User> spec = buildSpec(name, lastname, dni);

        Page<User> res = repository.findAll(spec, pageable);

        List<UserDTO> content = res.getContent().stream().map(user ->
                mapper.convertValue(user, UserDTO.class)).collect(Collectors.toList());

        return new PageDTO<>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber(),
                res.getSize()
        );
    }

    @Override
    public UserDTO save(UserDTO user) {
        User entity = mapper.convertValue(user, User.class);
        User saved = repository.save(entity);

        return mapper.convertValue(saved, UserDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        try {
            User user = repository.findById(id).orElseThrow();
            repository.deleteById(id);
        } catch (Exception e) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR, id));
        }
    }

    @Override
    public UserDTO update(Long id, UserDTO user) {
        if (repository.existsById(id)) {
            User mappedUser = mapper.convertValue(user, User.class);
            mappedUser.setId(id); // Aquí usamos setId
            User updated = repository.save(mappedUser);
            return mapper.convertValue(updated, UserDTO.class);
        } else {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR, id));
        }
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
