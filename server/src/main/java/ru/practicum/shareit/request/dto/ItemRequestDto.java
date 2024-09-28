package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class ItemRequestDto {
    Long id;
    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 512, message = "Описание не должно быть больше 512 символов")
    String description;
    User requestor;
    LocalDateTime created;
}
