package com.example.bankcards.controller;

import com.example.bankcards.controller.rest.CardRequestRestController;
import com.example.bankcards.dto.card.CardRequestDto;
import com.example.bankcards.service.CardRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CardRequestRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardRequestRestControllerTest extends BaseWebMvcTest{

    @MockitoBean
    private CardRequestService service;

    @Test
    void createActivateRequest_success() throws Exception {
        when(securityUtil.getCurrentUserId()).thenReturn(1L);
        when(service.createActivateRequest(any(), eq(1L)))
                .thenReturn(new CardRequestDto(1L, 1L, null, null, null, null, null));

        mockMvc.perform(post("/api/card-requests/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "type": "ACTIVATE", "requestedBalance": 1000 }
                                """))
                .andExpect(status().isCreated());
    }
}
