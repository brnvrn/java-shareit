package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    Long id;
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    User requestor;
    LocalDateTime created;
}
