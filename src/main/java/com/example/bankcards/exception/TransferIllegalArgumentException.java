package com.example.bankcards.exception;

public class TransferIllegalArgumentException extends DomainException {

    public TransferIllegalArgumentException(String message) {
        super(message);
    }

    public static TransferIllegalArgumentException sameCard(String cardNumber) {
        return new TransferIllegalArgumentException(
                String.format(
                        "Cannot transfer to the same card, cardNumber=%s",
                        cardNumber
                )
        );
    }
}

