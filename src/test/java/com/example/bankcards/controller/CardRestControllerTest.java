package com.example.bankcards.controller;

import com.example.bankcards.controller.rest.CardRestController;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardRestControllerTest extends BaseWebMvcTest{

    @MockitoBean
    private CardService cardService;

    @Test
    void createCard_adminAllowed() throws Exception {
        when(cardService.createCard(any()))
                .thenReturn(new CardDto(1L,
                        UUID.randomUUID(),
                        1L,
                        "**** **** **** 1234",
                        LocalDate.now().plusYears(4),
                        CardStatus.ACTIVE,
                        BigDecimal.TEN));

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "ownerId": 1, "initialBalance": 100 }
                                """))
                .andExpect(status().isCreated());
    }
}
