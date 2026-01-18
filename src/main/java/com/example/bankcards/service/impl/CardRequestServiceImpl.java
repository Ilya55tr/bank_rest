package com.example.bankcards.service.impl;

import com.example.bankcards.dto.card.CardRequestActivateDto;
import com.example.bankcards.dto.card.CardRequestBlockDto;
import com.example.bankcards.dto.card.CardRequestDto;
import com.example.bankcards.dto.mapper.CardRequestMapper;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specifications.CardRequestSpecifications;
import com.example.bankcards.service.CardRequestService;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.util.CryptoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CardRequestServiceImpl implements CardRequestService {

    private final CardRequestRepository repository;
    private final CardRequestMapper mapper;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CardNumberGenerator cardNumberGenerator;
    private final CryptoHelper cryptoHelper;


    @Override
    public CardRequestDto createActivateRequest(
            CardRequestActivateDto dto,
            Long userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        String cardNumber = cardNumberGenerator.generate();

        Card card = Card.builder()
                .publicNumber(UUID.randomUUID())
                .owner(user)
                .cardNumberEncrypted(cryptoHelper.encrypt(cardNumber))
                .last4(cardNumber.substring(12))
                .expirationDate(LocalDate.now().plusYears(4))
                .status(CardStatus.NOT_ACTIVE)
                .balance(dto.requestedBalance())
                .createdAt(LocalDateTime.now())
                .build();
        Card savedCard = cardRepository.save(card);
        CardRequest request = mapper.toEntity(dto, user, savedCard);
        return mapper.toDto(repository.save(request));
    }

    @Override
    public CardRequestDto createBlockRequest(
            CardRequestBlockDto dto,
            Long userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        Card card = cardRepository.findForUpdate(dto.cardId(), userId)
                .orElseThrow(() -> CardAccessDeniedException.notUsersCard(userId, dto.cardId()));

        CardRequest request = mapper.toEntity(dto, user, card);
        return mapper.toDto(repository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardRequestDto> getUserRequests(
            Long userId,
            Pageable pageable
    ) {
        return repository.findAllByUser_Id(userId, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardRequestDto> getAllRequests(
            Long userId,
            Long cardId,
            RequestType type,
            RequestStatus status,
            BigDecimal requestedBalance,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            Pageable pageable
    ) {
        Specification<CardRequest> spec = CardRequestSpecifications.userId(userId)
                .and(
                        CardRequestSpecifications.cardId(cardId)
                ).and(
                        CardRequestSpecifications.type(type)
                ).and(
                        CardRequestSpecifications.status(status)
                ).and(
                        CardRequestSpecifications.requestedBalance(requestedBalance)
                ).and(
                        CardRequestSpecifications.createdAtFrom(createdFrom)
                ).and(
                        CardRequestSpecifications.createdAtTo(createdTo)
                );

        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }



    @Override
    @Transactional
    public void approve(Long cardRequestId) {
        CardRequest request = getPendingRequest(cardRequestId);
        Card card = request.getCard();

        switch (request.getType()) {
            case ACTIVATE -> card.setStatus(CardStatus.ACTIVE);
            case BLOCK -> card.setStatus(CardStatus.BLOCKED);
            default -> throw new CardIllegalStateException(
                    "Unsupported request type: " + request.getType()
            );
        }
        request.setStatus(RequestStatus.APPROVED);
    }

    @Override
    @Transactional
    public void reject(Long cardRequestId) {
        CardRequest request = getPendingRequest(cardRequestId);
        request.setStatus(RequestStatus.REJECTED);
    }
    private CardRequest getPendingRequest(Long cardRequestId) {
        CardRequest request = repository.findById(cardRequestId)
                .orElseThrow(() -> CardRequestNotFoundException.byId(cardRequestId));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new CardIllegalStateException(
                    "Request already processed, status = " + request.getStatus()
            );
        }
        return request;
    }

}

