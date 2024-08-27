package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class CommentDto {
    private long id;
    @NotBlank
    @Size(max = 512, message = "Комментарий не должен быть больше 512 символов")
    private String text;
    private long itemId;
    private String authorName;
    private LocalDateTime created;
}