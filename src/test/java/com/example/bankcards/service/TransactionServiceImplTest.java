package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.TransactionMapper;
import com.example.bankcards.dto.transaction.TransactionCreateDto;
import com.example.bankcards.dto.transaction.TransactionDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.CardIllegalStateException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;

    @Test
    void transfer_success() {
        Long userId = 1L;

        UUID fromUuid = UUID.randomUUID();
        UUID toUuid = UUID.randomUUID();

        Card from = Card.builder()
                .id(1L)
                .publicNumber(fromUuid)
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        Card to = Card.builder()
                .id(2L)
                .publicNumber(toUuid)
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        TransactionCreateDto dto = new TransactionCreateDto(
                fromUuid,
                toUuid,
                BigDecimal.valueOf(500)
        );

        when(cardRepository.findForUpdateByPublicNumberAndOwner_Id(fromUuid, userId))
                .thenReturn(Optional.of(from));

        when(cardRepository.findForUpdateByPublicNumberAndOwner_Id(toUuid, userId))
                .thenReturn(Optional.of(to));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(transactionMapper.toDto(any(Transaction.class)))
                .thenReturn(mock(TransactionDto.class));

        TransactionDto result = service.transfer(userId, dto);

        assertEquals(BigDecimal.valueOf(500), from.getBalance());
        assertEquals(BigDecimal.valueOf(500), to.getBalance());
        assertNotNull(result);
    }

    @Test
    void transfer_insufficientFunds_throwsException() {
        Long userId = 1L;

        UUID fromUuid = UUID.randomUUID();
        UUID toUuid = UUID.randomUUID();

        Card from = Card.builder()
                .id(1L)
                .publicNumber(fromUuid)
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(100))
                .build();

        Card to = Card.builder()
                .id(2L)
                .publicNumber(toUuid)
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        TransactionCreateDto dto = new TransactionCreateDto(
                fromUuid,
                toUuid,
                BigDecimal.valueOf(500)
        );

        when(cardRepository.findForUpdateByPublicNumberAndOwner_Id(fromUuid, userId))
                .thenReturn(Optional.of(from));

        when(cardRepository.findForUpdateByPublicNumberAndOwner_Id(toUuid, userId))
                .thenReturn(Optional.of(to));

        assertThrows(
                CardIllegalStateException.class,
                () -> service.transfer(userId, dto)
        );
    }
}
