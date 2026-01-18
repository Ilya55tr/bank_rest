package com.example.bankcards.service;
import com.example.bankcards.dto.security.JwtAuthenticationDto;
import com.example.bankcards.dto.security.RefreshTokenDto;
import com.example.bankcards.dto.user.LoginDto;
import com.example.bankcards.dto.user.RegisterDto;
import com.example.bankcards.dto.user.UpdateUserDto;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

public interface UserService {

    Page<UserDto> getUsers(
            String email,
            String firstname,
            String lastname,
            Role role,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            Pageable pageable
    );

    UserDto getById(Long id);

    User getByEmail(String email);

    UserDto createUser(RegisterDto dto);

    UserDto updateUser(Long id, UpdateUserDto dto);

    JwtAuthenticationDto singIn(LoginDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException;

}
