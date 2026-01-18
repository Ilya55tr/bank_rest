package com.example.bankcards.controller;

import com.example.bankcards.security.CustomUserDetailsService;
import com.example.bankcards.security.JwtFilter;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


public abstract class BaseWebMvcTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected  SecurityUtil securityUtil;


    @MockitoBean
    protected  JwtService jwtService;

    @MockitoBean
    protected CustomUserDetailsService customUserDetailsService;
}
