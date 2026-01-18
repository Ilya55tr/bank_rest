package com.example.bankcards.repository;

import com.example.bankcards.dto.card.CardRequestDto;
import com.example.bankcards.entity.CardRequest;
import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.entity.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CardRequestRepository
        extends JpaRepository<CardRequest, Long>, JpaSpecificationExecutor<CardRequest> {

    Page<CardRequest> findAllByUser_Id(Long userId, Pageable pageable);
}