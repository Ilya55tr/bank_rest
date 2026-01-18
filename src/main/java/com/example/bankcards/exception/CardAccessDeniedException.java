package com.example.bankcards.exception;

public class CardAccessDeniedException extends DomainException {

    public CardAccessDeniedException(String message) {
        super(message);
    }

    public static CardAccessDeniedException notUsersCard(
            Long userId,
            String cardNumber
    ) {
        return new CardAccessDeniedException(
                String.format(
                        "Not user's card, userId=%d, cardNumber=%s",
                        userId,
                        cardNumber
                )
        );
    }

    public static CardAccessDeniedException notUsersCard(
            Long userId,
            Long cardId
    ) {
        return new CardAccessDeniedException(
                String.format(
                        "Not user's card, userId=%d, cardNumber=%d",
                        userId,
                        cardId
                )
        );
    }
}


