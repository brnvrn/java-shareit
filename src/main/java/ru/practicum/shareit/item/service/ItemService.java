package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> findAll(Long userId);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> searchItems(Long id, String text);
}
