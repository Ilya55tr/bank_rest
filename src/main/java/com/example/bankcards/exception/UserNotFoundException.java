package com.example.bankcards.exception;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(Long id) {
        super("User not found, id=" + id);
    }

    public UserNotFoundException(String username) {
        super("User not found, email=" + username);
    }
}


