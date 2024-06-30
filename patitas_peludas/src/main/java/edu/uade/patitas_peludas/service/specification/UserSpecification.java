package edu.uade.patitas_peludas.service.specification;

import edu.uade.patitas_peludas.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> nameSpec(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<User> lastnameSpec(String lastname) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("lastname"), "%" + lastname + "%");
    }

    public static Specification<User> emailSpec(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }
}
