package com.example.bankcards.exception;

public class CardNotFoundException extends DomainException {

    private CardNotFoundException(String message) {
        super(message);
    }

    public static CardNotFoundException byId(Long id) {
        return new CardNotFoundException(
                String.format("Card not found, id=%d", id)
        );
    }
}

