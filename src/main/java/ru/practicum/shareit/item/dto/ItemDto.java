package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank
    @Size(max = 255, message = "Имя не должно быть больше 255 символов")
    private String name;
    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 255, message = "Имя не должно быть больше 255 символов")
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private List<Comment> comments;
}
