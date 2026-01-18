package com.example.bankcards.repository.specifications;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class UserSpecifications {

    public static Specification<User> emailLike(String email) {
        return (root, query, cb) ->
                email == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                );
    }

    public static Specification<User> firstnameLike(String firstname) {
        return (root, query, cb) ->
                firstname == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("firstname")),
                        "%" + firstname.toLowerCase() + "%"
                );
    }

    public static Specification<User> lastnameLike(String lastname) {
        return (root, query, cb) ->
                lastname == null
                        ? null
                        : cb.like(
                        cb.lower(root.get("lastname")),
                        "%" + lastname.toLowerCase() + "%"
                );
    }

    public static Specification<User> role(Role role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role);
    }

    public static Specification<User> createdAfter(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<User> createdBefore(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }
}
