package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.user.RegisterDto;
import com.example.bankcards.dto.user.UpdateUserDto;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserIllegalStateException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createUser_success() {
        RegisterDto dto = new RegisterDto(
                "test@mail.com",
                "John",
                "Doe",
                "password"
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userMapper.toDto(any())).thenReturn(mock(UserDto.class));

        UserDto result = service.createUser(dto);

        assertNotNull(result);
    }

    @Test
    void createUser_emailExists_throwsException() {
        RegisterDto dto = new RegisterDto(
                "test@mail.com",
                "John",
                "Doe",
                "password"
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(
                UserIllegalStateException.class,
                () -> service.createUser(dto)
        );
    }

    @Test
    void updateUser_success() {
        UpdateUserDto dto = new UpdateUserDto("New", "Name", Role.ADMIN);

        User user = User.builder().id(1L).build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user))
                .thenReturn(mock(UserDto.class));

        UserDto result = service.updateUser(1L, dto);

        assertNotNull(result);
        assertEquals("New", user.getFirstname());
        assertEquals("Name", user.getLastname());
        assertEquals(Role.ADMIN, user.getRole());
    }
}

