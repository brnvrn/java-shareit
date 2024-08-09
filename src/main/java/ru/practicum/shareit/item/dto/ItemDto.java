package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
@AllArgsConstructor
public class ItemDto {
    long id;
    @NotBlank
    @Size(max = 100, message = "Имя не должно быть больше 100 символов")
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 200, message = "Имя не должно быть больше 200 символов")
    String description;
    @NotNull
    Boolean available;
    Long owner;
    ItemRequest request;
}
