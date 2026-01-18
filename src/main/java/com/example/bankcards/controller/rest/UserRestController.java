package com.example.bankcards.controller.rest;

import com.example.bankcards.dto.user.RegisterDto;
import com.example.bankcards.dto.user.UpdateUserDto;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Users", description = "Управление пользователями (ADMIN)")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserRestController {

    private final UserService userService;

    @Operation(
            summary = "Получить список пользователей (только ADMIN)",
            description = "Фильтрация по email, имени, роли и дате создания"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей"),
            @ApiResponse(responseCode = "403", description = "Нет прав ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserDto> getUsers(
            @Parameter(description = "Email пользователя")
            @RequestParam(required = false) String email,

            @Parameter(description = "Имя")
            @RequestParam(required = false) String firstname,

            @Parameter(description = "Фамилия")
            @RequestParam(required = false) String lastname,

            @Parameter(description = "Роль пользователя")
            @RequestParam(required = false) Role role,

            @Parameter(description = "Дата создания ОТ")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdFrom,

            @Parameter(description = "Дата создания ДО")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdTo,

            Pageable pageable
    ) {
        return userService.getUsers(
                email, firstname, lastname, role, createdFrom, createdTo, pageable
        );
    }

    @Operation(summary = "Получить пользователя по ID (только ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDto getById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id
    ) {
        return userService.getById(id);
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт пользователя с ролью USER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует")
    })
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @Parameter(description = "Данные для регистрации пользователя")
            @Valid @RequestBody RegisterDto registerDto
    ) {
        return userService.createUser(registerDto);
    }


    @Operation(
            summary = "Обновить пользователя (ADMIN)",
            description = "Позволяет изменить имя, фамилию и роль пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "403", description = "Нет прав ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserDto updateUser(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id,

            @Valid
            @RequestBody UpdateUserDto dto
    ) {
        return userService.updateUser(id, dto);
    }

}
