package com.example.bankcards.dto.mapper;
import com.example.bankcards.dto.transaction.TransactionDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import org.springframework.stereotype.Component;


@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction tx) {
        return new TransactionDto(
                tx.getId(),
                mask(tx.getFromCard()),
                mask(tx.getToCard()),
                tx.getAmount(),
                tx.getStatus(),
                tx.getCreatedAt()
        );
    }

    private String mask(Card card) {
        return "**** **** **** " + card.getLast4();
    }
}


