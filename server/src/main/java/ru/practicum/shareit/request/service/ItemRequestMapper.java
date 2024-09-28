package ru.practicum.shareit.request.service;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    ItemRequest toItemRequest(ItemRequestNewDto itemRequestNewDto);

    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest);

    List<ItemRequestResponseDto> toListItemRequestResponseDto(List<ItemRequest> itemRequestList);
}
