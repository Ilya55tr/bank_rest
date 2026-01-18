package com.example.bankcards.exception;

public class CardRequestNotFoundException extends DomainException {

    private CardRequestNotFoundException(String message) {
        super(message);
    }

    public static CardRequestNotFoundException byId(Long id) {
        return new CardRequestNotFoundException(
                String.format("Card not found, id=%d", id)
        );
    }
}

