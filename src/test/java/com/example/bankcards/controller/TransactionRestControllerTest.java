package com.example.bankcards.controller;

import com.example.bankcards.controller.rest.TransactionRestController;
import com.example.bankcards.dto.transaction.TransactionCreateDto;
import com.example.bankcards.dto.transaction.TransactionDto;
import com.example.bankcards.entity.TransactionStatus;
import com.example.bankcards.service.TransactionService;
import com.example.bankcards.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionRestControllerTest extends BaseWebMvcTest {

    @MockitoBean
    private TransactionService service;

    @Test
    void transfer_success() throws Exception {

        UUID from = UUID.randomUUID();
        UUID to = UUID.randomUUID();

        when(securityUtil.getCurrentUserId()).thenReturn(1L);

        when(service.transfer(eq(1L), any(TransactionCreateDto.class)))
                .thenReturn(new TransactionDto(
                        1L,
                        "**** **** **** 1234",
                        "**** **** **** 5678",
                        BigDecimal.TEN,
                        TransactionStatus.SUCCESS,
                        LocalDateTime.now()
                ));

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fromCardNumber": "%s",
                                  "toCardNumber": "%s",
                                  "amount": 10
                                }
                                """.formatted(from, to)))
                .andExpect(status().isCreated());
    }
}
