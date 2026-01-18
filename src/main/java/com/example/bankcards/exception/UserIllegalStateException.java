package com.example.bankcards.exception;

public class UserIllegalStateException extends DomainException {

    private UserIllegalStateException(String message) {
        super(message);
    }

    public static UserIllegalStateException usernameAlreadyExists(String username) {
        return new UserIllegalStateException(
                String.format("Username already exists, email=%s", username)
        );
    }
}

