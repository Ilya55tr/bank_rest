package com.example.bankcards.repository.specifications;

import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.TransactionStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class TransactionSpecifications {

    public static Specification<Transaction> owner(Long userId) {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("fromCard").get("owner").get("id"), userId),
                cb.equal(root.get("toCard").get("owner").get("id"), userId)
        );
    }

    public static Specification<Transaction> status(TransactionStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Transaction> createdAfter(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<Transaction> createdBefore(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }
}
