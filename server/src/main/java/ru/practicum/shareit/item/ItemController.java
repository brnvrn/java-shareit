package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    public static final String USER_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addNewItem(@RequestHeader(USER_HEADER) Long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Поступил POST-запрос на добавление предмета");
        return itemService.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Поступил PATCH-запрос на обновление предмета от пользователя с id = {}", itemId);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemCommentDto getItemById(@RequestHeader(USER_HEADER) Long userId,
                                      @PathVariable Long itemId) {
        log.info("Поступил GET-запрос на получение предмета с id = {} от пользователя c id = {}", itemId, userId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("Поступил GET-запрос на получение всех предметов пользователя c id = {}", userId);
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(USER_HEADER) Long userId,
                                     @RequestParam(required = false) String text) {
        log.info("Поступил GET-запрос на поиск предмета от пользователя c id = {}", userId);
        return itemService.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Post-запрос addComment");
        return itemService.addComment(userId, itemId, commentDto);
    }
}

