package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardRequestActivateDto;
import com.example.bankcards.dto.card.CardRequestBlockDto;
import com.example.bankcards.dto.card.CardRequestDto;
import com.example.bankcards.dto.mapper.CardRequestMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardAccessDeniedException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.CardRequestServiceImpl;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.util.CryptoHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardRequestServiceImplTest {

    @InjectMocks
    private CardRequestServiceImpl service;

    @Mock
    private CardRequestRepository repository;
    @Mock
    private CardRequestMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;

    @Mock
    private CryptoHelper cryptoHelper;

    @Mock
    private CardNumberGenerator cardNumberGenerator;

    @Test
    void createActivateRequest_success() {
        Long userId = 1L;
        User user = new User();

        CardRequestActivateDto dto = mock(CardRequestActivateDto.class);
        CardRequest entity = new CardRequest();
        CardRequestDto response = mock(CardRequestDto.class);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(cardNumberGenerator.generate())
                .thenReturn("1234567812345678");

        when(cryptoHelper.encrypt(anyString()))
                .thenReturn("encrypted".getBytes(StandardCharsets.UTF_8));

        when(mapper.toEntity(
                eq(dto),
                eq(user),
                isNull()
        )).thenReturn(entity);

        when(repository.save(entity))
                .thenReturn(entity);

        when(mapper.toDto(entity))
                .thenReturn(response);

        CardRequestDto result = service.createActivateRequest(dto, userId);

        assertEquals(response, result);
    }



    @Test
    void createBlockRequest_notUsersCard_throwsException() {
        Long userId = 1L;
        Long cardId = 10L;

        User user = new User();
        CardRequestBlockDto dto = mock(CardRequestBlockDto.class);
        when(dto.cardId()).thenReturn(cardId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cardRepository.findForUpdate(cardId, userId)).thenReturn(Optional.empty());

        assertThrows(
                CardAccessDeniedException.class,
                () -> service.createBlockRequest(dto, userId)
        );
    }
}
