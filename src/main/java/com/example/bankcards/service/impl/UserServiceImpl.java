package com.example.bankcards.service.impl;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.security.JwtAuthenticationDto;
import com.example.bankcards.dto.security.RefreshTokenDto;
import com.example.bankcards.dto.user.LoginDto;
import com.example.bankcards.dto.user.RegisterDto;
import com.example.bankcards.dto.user.UpdateUserDto;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserIllegalStateException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specifications.UserSpecifications;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationDto singIn(LoginDto loginDto) throws AuthenticationException {
        User user = getByEmail(loginDto.email());
        return jwtService.generateAuthToken(user.getEmail());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            User user = getByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        }
        throw new  AuthenticationException("Invalid refresh token");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(
            String email,
            String firstname,
            String lastname,
            Role role,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            Pageable pageable
    ) {
        Specification<User> spec = UserSpecifications.emailLike(email)
                .and(UserSpecifications.firstnameLike(firstname))
                .and(UserSpecifications.lastnameLike(lastname))
                .and(UserSpecifications.role(role))
                .and(UserSpecifications.createdAfter(createdFrom))
                .and(UserSpecifications.createdBefore(createdTo));

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setRole(dto.role());

        return userMapper.toDto(user);
    }

    @Override
    public UserDto createUser(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw UserIllegalStateException.usernameAlreadyExists(dto.email());
        }

        User user = User.builder()
                .email(dto.email())
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        return userMapper.toDto(userRepository.save(user));
    }
}

