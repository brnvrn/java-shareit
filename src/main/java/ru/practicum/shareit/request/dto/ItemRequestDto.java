package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    Long id;
    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 100, message = "Описание не должно быть больше 100 символов")
    String description;
    User requestor;
    LocalDateTime created;
}
