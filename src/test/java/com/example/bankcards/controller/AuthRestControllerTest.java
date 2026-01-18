package com.example.bankcards.controller;

import com.example.bankcards.controller.rest.AuthRestController;
import com.example.bankcards.dto.security.JwtAuthenticationDto;
import com.example.bankcards.security.CustomUserDetailsService;

import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthRestControllerTest extends  BaseWebMvcTest{

    @MockitoBean
    private UserService userService;

    @Test
    void login_success() throws Exception {
        JwtAuthenticationDto jwt = new JwtAuthenticationDto();
        jwt.setToken("access");
        jwt.setRefreshToken("refresh");

        when(userService.singIn(any())).thenReturn(jwt);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "test@mail.com",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("access"))
                .andExpect(jsonPath("$.refreshToken").value("refresh"));
    }
}
