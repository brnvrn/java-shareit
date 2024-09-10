package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Поступил POST-запрос от пользователя с id: {} на добавление вещи", userId);
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader(USER_ID_HEADER) @Positive long userId) {
        log.info("Поступил GET-запрос на получение списка своих запросов id: {}", userId);
        return itemRequestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                                     @RequestParam(defaultValue = "0", required = false) int from,
                                                     @RequestParam(defaultValue = "20", required = false) int size) {
        log.info("Поступил GET-запрос на получение списка запросов, созданных другими пользователями");
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getOneItemRequest(@RequestHeader(USER_ID_HEADER) @Positive long userId,
                                                    @PathVariable @Positive long requestId) {
        log.info("Поступил GET-запрос на получение данных об одном конкретном запросе");
        return itemRequestClient.getOneItemRequest(userId, requestId);
    }
}
