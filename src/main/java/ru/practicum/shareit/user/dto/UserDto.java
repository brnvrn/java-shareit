package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    long id;
    @NotBlank
    @Size(max = 100, message = "Имя не должно быть больше 100 символов")
    String name;
    @NotBlank
    @Email
    @Size(max = 30, message = "Эмейл не должен быть больше 30 символов")
    String email;
}
