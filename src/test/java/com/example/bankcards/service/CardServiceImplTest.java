package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardCreateDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.util.CryptoHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl service;

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private CardNumberGenerator cardNumberGenerator;
    @Mock
    private CryptoHelper cryptoHelper;

    @Test
    void createCard_success() {
        Long userId = 1L;
        User user = new User();
        CardCreateDto dto = new CardCreateDto(userId, BigDecimal.valueOf(1000));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cardNumberGenerator.generate()).thenReturn("1234567812345678");
        when(cryptoHelper.encrypt(any())).thenReturn("encrypted".getBytes());
        when(cardRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(cardMapper.toDto(any())).thenReturn(mock(CardDto.class));

        CardDto result = service.createCard(dto);

        assertNotNull(result);
    }

    @Test
    void deleteCard_notFound_throwsException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                CardNotFoundException.class,
                () -> service.deleteCard(1L)
        );
    }
}

