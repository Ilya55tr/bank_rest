package com.example.bankcards.repository.specifications;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CardSpecifications {

    public static Specification<Card> owner(Long ownerId) {
        return (root, query, cb) ->
                cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Card> status(CardStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Card> expirationBefore(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.lessThan(root.get("expirationDate"), date);
    }
}

