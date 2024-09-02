package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    @NotBlank
    @Size(max = 255, message = "Имя не должно быть больше 255 символов")
    String name;
    @NotBlank
    @Email
    @Size(max = 50, message = "Эмейл не должен быть больше 50 символов")
    String email;
}
