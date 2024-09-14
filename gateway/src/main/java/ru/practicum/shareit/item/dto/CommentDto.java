package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    @NotBlank
    @Size(max = 512, message = "Комментарий не должен быть больше 512 символов")
    String text;
}