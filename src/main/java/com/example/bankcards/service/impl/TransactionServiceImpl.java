package com.example.bankcards.service.impl;

import com.example.bankcards.dto.transaction.TransactionCreateDto;
import com.example.bankcards.dto.transaction.TransactionDto;
import com.example.bankcards.dto.mapper.TransactionMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.TransactionStatus;
import com.example.bankcards.exception.CardAccessDeniedException;
import com.example.bankcards.exception.CardIllegalStateException;
import com.example.bankcards.exception.TransferIllegalArgumentException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.repository.specifications.TransactionSpecifications;
import com.example.bankcards.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getAllTransactions(
            TransactionStatus status,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {
        Specification<Transaction> spec = TransactionSpecifications.status(status)
                .and(TransactionSpecifications.createdAfter(from))
                .and(TransactionSpecifications.createdBefore(to));

        return transactionRepository.findAll(spec, pageable)
                .map(transactionMapper::toDto);
    }


    @Override
    @Transactional
    public TransactionDto transfer(Long userId, TransactionCreateDto request) {

        if (request.fromCardNumber().equals(request.toCardNumber())) {
            throw TransferIllegalArgumentException.sameCard(request.fromCardNumber().toString());
        }

        Card fromCard = cardRepository
                .findForUpdateByPublicNumberAndOwner_Id(request.fromCardNumber(), userId)
                .orElseThrow(() ->
                        CardAccessDeniedException.notUsersCard(userId, String.valueOf(request.fromCardNumber()))
                );

        Card toCard = cardRepository
                .findForUpdateByPublicNumberAndOwner_Id(request.toCardNumber(), userId)
                .orElseThrow(() ->
                        CardAccessDeniedException.notUsersCard(userId, String.valueOf(request.fromCardNumber()))
                );

        validateCards(fromCard, toCard, request.amount());

        fromCard.setBalance(fromCard.getBalance().subtract(request.amount()));
        toCard.setBalance(toCard.getBalance().add(request.amount()));

        Transaction transaction = Transaction.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(request.amount())
                .status(TransactionStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        return transactionMapper.toDto(transactionRepository.save(transaction));
    }



    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDto> getUserTransactions(
            Long userId,
            TransactionStatus status,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {
        Specification<Transaction> spec = TransactionSpecifications.owner(userId)
                .and(TransactionSpecifications.status(status))
                .and(TransactionSpecifications.createdAfter(from))
                .and(TransactionSpecifications.createdBefore(to));

        return transactionRepository.findAll(spec, pageable)
                .map(transactionMapper::toDto);
    }


    private void validateCards(
            Card from,
            Card to,
            BigDecimal amount
    ) {
        if (from.getStatus() != CardStatus.ACTIVE &&
            to.getStatus() != CardStatus.ACTIVE) {

            throw CardIllegalStateException.bothNotActive(
                    from.getId(),
                    to.getId()
            );
        }

        if (from.getStatus() != CardStatus.ACTIVE) {
            throw CardIllegalStateException.fromCardNotActive(from.getId());
        }

        if (to.getStatus() != CardStatus.ACTIVE) {
            throw CardIllegalStateException.toCardNotActive(to.getId());
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw CardIllegalStateException.insufficientFunds(from.getId(), from.getBalance(), amount);
        }
    }
}
