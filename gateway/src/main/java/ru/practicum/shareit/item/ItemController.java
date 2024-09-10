package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                             @Valid @RequestBody ItemRequestDto itemDto) {
        log.info("Поступил POST-запрос на добавление предмета");
        return itemClient.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                             @Valid @RequestBody ItemUpdateRequestDto itemUpdateRequestDto,
                                             @PathVariable @Positive long itemId) {
        log.info("Поступил PATCH-запрос на обновление предмета от пользователя с id = {}", itemId);
        return itemClient.updateItem(userId, itemId, itemUpdateRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(USER_ID_HEADER) @Positive long userId) {
        log.info("Поступил GET-запрос на получение всех предметов пользователя c id = {}", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                              @PathVariable @Positive long itemId) {
        log.info("Поступил GET-запрос на получение предмета с id = {} от пользователя c id = {}", itemId, userId);
        return itemClient.getItemById(itemId, userId);

    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text) {
        log.info("Поступил GET-запрос на поиск предмета");
        return itemClient.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) @Positive Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Поступил POST-запрос на добавление нового комментария");
        return itemClient.addComment(userId, itemId, commentDto);
    }
}