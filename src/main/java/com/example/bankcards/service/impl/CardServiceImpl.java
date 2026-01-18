package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.CardCreateDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specifications.CardSpecifications;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.util.CryptoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardNumberGenerator cardNumberGenerator;
    private final CryptoHelper cryptoHelper;

    @Override
    public CardDto createCard(CardCreateDto dto) {

        User owner = userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new UserNotFoundException(dto.ownerId()));

        String cardNumber = cardNumberGenerator.generate();

        Card card = Card.builder()
                .publicNumber(UUID.randomUUID())
                .owner(owner)
                .cardNumberEncrypted(cryptoHelper.encrypt(cardNumber))
                .last4(cardNumber.substring(12))
                .expirationDate(LocalDate.now().plusYears(4))
                .status(CardStatus.ACTIVE)
                .balance(dto.initialBalance())
                .createdAt(LocalDateTime.now())
                .build();

        return cardMapper.toDto(cardRepository.save(card));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getAllCards(
            CardStatus status,
            LocalDate expirationBefore,
            Pageable pageable
    ) {
        Specification<Card> spec = CardSpecifications.status(status)
                .and(CardSpecifications.expirationBefore(expirationBefore));

        return cardRepository.findAll(spec, pageable)
                .map(cardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDto getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> CardNotFoundException.byId(cardId));

        return cardMapper.toDto(card);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getUserCards(
            Long userId,
            CardStatus status,
            LocalDate expirationBefore,
            Pageable pageable
    ) {

        Specification<Card> spec = CardSpecifications.owner(userId)
                .and(CardSpecifications.status(status))
                .and(CardSpecifications.expirationBefore(expirationBefore));

        return cardRepository.findAll(spec, pageable)
                .map(cardMapper::toDto);
    }

    @Override
    public CardDto getUserCard(Long userId, Long cardId) {

        Card card = cardRepository.findForUpdate(cardId, userId)
                .orElseThrow(() -> CardNotFoundException.byId(cardId));

        return cardMapper.toDto(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> CardNotFoundException.byId(cardId));
        cardRepository.delete(card);
    }



}
