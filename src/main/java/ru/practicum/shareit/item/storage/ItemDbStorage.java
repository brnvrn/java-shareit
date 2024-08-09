package ru.practicum.shareit.item.storage;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDbStorage implements ItemStorage {
    private final UserStorage userStorage;
    private final Map<Long, Item> items = new HashMap<>();
    private long generatorId = 1;

    @Override
    public Item addNewItem(long id, Item item) {
        if (items.containsKey(item.getId())) {
            throw new ValidationException("Предмет не существует");
        } else {
            long itemId = ++generatorId;
            item.setId(itemId);
            item.setOwner(id);
            items.put(item.getId(), item);
            return item;
        }
    }

    @Override
    public Item update(Long userId, Long itemId, Item itemDto) {
        User user = userStorage.getUserById(userId);
        Item item = items.get(itemId);

        if (user.getId() != item.getOwner()) {
            throw new IllegalArgumentException("Вы не являетесь владельцем этого предмета");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getRequest() != null) {
            item.setRequest(itemDto.getRequest());
        }

        return item;
    }

    @Override
    public List<Item> findAll(Long userId) {
        List<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(userId)) {
                userItems.add(item);
            }
        }
        return userItems;
    }

    @Override
    public Item getItemById(Long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new ValidationException("Предмет не найден");
        }
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(item -> item.getAvailable() && (
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }
}
