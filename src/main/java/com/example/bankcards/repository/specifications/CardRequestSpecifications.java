package com.example.bankcards.repository.specifications;

import com.example.bankcards.entity.CardRequest;
import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.entity.RequestType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardRequestSpecifications {

    public static Specification<CardRequest> userId(Long userId) {
        return (root, query, cb) ->
                userId == null ? null :
                        cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<CardRequest> cardId(Long cardId) {
        return (root, query, cb) ->
                cardId == null ? null :
                        cb.equal(root.get("card").get("id"), cardId);
    }

    public static Specification<CardRequest> type(RequestType type) {
        return (root, query, cb) ->
                type == null ? null :
                        cb.equal(root.get("type"), type);
    }

    public static Specification<CardRequest> status(RequestStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<CardRequest> requestedBalance(BigDecimal balance) {
        return (root, query, cb) ->
                balance == null ? null :
                        cb.equal(root.get("requestedBalance"), balance);
    }

    public static Specification<CardRequest> createdAtFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null :
                        cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<CardRequest> createdAtTo(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null :
                        cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }
}
