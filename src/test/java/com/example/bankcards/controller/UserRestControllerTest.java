package com.example.bankcards.controller;

import com.example.bankcards.controller.rest.UserRestController;
import com.example.bankcards.dto.user.UpdateUserDto;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRestControllerTest extends BaseWebMvcTest{

    @MockitoBean
    private UserService userService;

    @Test
    void getUsers_adminAllowed() throws Exception {
        when(userService.getUsers(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_success() throws Exception {

        when(userService.updateUser(eq(1L), any(UpdateUserDto.class)))
                .thenReturn(mock(UserDto.class));

        mockMvc.perform(put("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstname": "New",
                                  "lastname": "Name",
                                  "role": "ADMIN"
                                }
                                """))
                .andExpect(status().isOk());
    }
}
