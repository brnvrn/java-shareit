package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
@AllArgsConstructor
public class ItemDto {
    long id;
    @NotBlank
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    @NotNull
    Boolean available;
    Long owner;
    ItemRequest request;
}
