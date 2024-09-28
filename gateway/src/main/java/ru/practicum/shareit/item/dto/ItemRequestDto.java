package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.User;

@Data
@Builder
public class ItemRequestDto {
    Long id;
    String name;
    String description;
    User owner;
    Boolean available;
    Long requestId;
}
