package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addNewItem(long id, Item item);

    Item update(Long userId, Long itemId, Item itemDto);

    List<Item> findAll(Long userId);

    Item getItemById(Long itemId);

    List<Item> searchItems(String text);
}
