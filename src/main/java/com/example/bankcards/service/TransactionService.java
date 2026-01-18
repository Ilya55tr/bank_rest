package com.example.bankcards.service;

import com.example.bankcards.dto.transaction.TransactionCreateDto;
import com.example.bankcards.dto.transaction.TransactionDto;
import com.example.bankcards.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TransactionService {

    Page<TransactionDto> getAllTransactions(
            TransactionStatus status,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

    TransactionDto transfer(
            Long userId,
            TransactionCreateDto request
    );

    Page<TransactionDto> getUserTransactions(
            Long userId,
            TransactionStatus status,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );
}
