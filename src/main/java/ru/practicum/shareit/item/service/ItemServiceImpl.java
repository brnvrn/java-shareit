package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        if (userStorage.getUserById(userId) == null) {
            throw new IllegalArgumentException("Такой пользователь не найден");
        }
        Item item = itemStorage.addNewItem(userId, ItemMapper.toItem(itemDto));
        log.info("Добавление нового предмета пользователя с id = {}", userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        if (userStorage.getUserById(userId) == null) {
            throw new IllegalArgumentException("Такой пользователь не найден");
        }
        Item item = itemStorage.update(userId, itemId, ItemMapper.toItem(itemDto));
        log.info("Обновление предмета с id = {} пользователя с id = {}", itemId, userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = itemStorage.getItemById(itemId);
        log.info("Получение предмета с id = {} пользователя с id = {}", itemId, userId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        log.info("Получение всех предметов пользователя с id = {}", userId);
        return itemStorage.findAll(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(Long userId, String text) {
        if (userStorage.getUserById(userId) == null) {
            throw new IllegalArgumentException("Такой пользователь не найден");
        }

        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("ПОиск предметов пользователя с id = {}", userId);
        return itemStorage.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
