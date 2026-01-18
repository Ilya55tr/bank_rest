package com.example.bankcards.exception;

import java.math.BigDecimal;

public class CardIllegalStateException extends DomainException {

    public CardIllegalStateException(String message) {
        super(message);
    }

    public static CardIllegalStateException notActive(Long cardId) {
        return new CardIllegalStateException(
                String.format("Card is not active, publicNumber=%d", cardId)
        );
    }

    public static CardIllegalStateException bothNotActive(
            Long fromCardId,
            Long toCardId
    ) {
        return new CardIllegalStateException(
                String.format(
                        "Both cards are not active, fromCardId=%d, toCardId=%d",
                        fromCardId,
                        toCardId
                )
        );
    }

    public static CardIllegalStateException fromCardNotActive(Long cardId) {
        return new CardIllegalStateException(
                String.format("From card is not active, publicNumber=%d", cardId)
        );
    }

    public static CardIllegalStateException toCardNotActive(Long cardId) {
        return new CardIllegalStateException(
                String.format("To card is not active, publicNumber=%d", cardId)
        );
    }

    public static CardIllegalStateException insufficientFunds(
            Long cardId,
            BigDecimal balance,
            BigDecimal amount
    ) {
        return new CardIllegalStateException(
                String.format(
                        "Insufficient funds, publicNumber=%d, balance=%s, amount=%s",
                        cardId,
                        balance,
                        amount
                )
        );
    }
}
