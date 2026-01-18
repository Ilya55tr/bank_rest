package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository
        extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Card c where c.id = :id and c.owner.id = :ownerId")
    Optional<Card> findForUpdate(Long id, Long ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Card> findForUpdateByPublicNumberAndOwner_Id(UUID publicNumber, Long userId);

}
