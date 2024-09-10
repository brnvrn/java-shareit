package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    public static final String USER_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ItemRequestDto addNewItemRequest(@RequestHeader(USER_HEADER) @Positive Long userId,
                                            @RequestBody ItemRequestNewDto itemRequestNewDto) {
        log.info("Поступил POST-запрос от пользователя с id: {} на добавление вещи", userId);
        return itemRequestService.addNewItemRequest(userId, itemRequestNewDto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getItemRequestByUser(@RequestHeader(USER_HEADER) @Positive Long userId) {
        log.info("Поступил GET-запрос на получение списка своих запросов id: {}", userId);
        return itemRequestService.getItemRequestByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getItemRequest(@RequestHeader(USER_HEADER) @Positive Long userId,
                                                 @PathVariable @Positive Long requestId) {
        log.info("Поступил GET-запрос на получение данных об одном конкретном запросе");
        return itemRequestService.getItemRequest(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(USER_HEADER) @Positive Long userId,
                                                   @RequestParam(defaultValue = "0", required = false) int from,
                                                   @RequestParam(defaultValue = "20", required = false) int size) {
        log.info("Поступил GET-запрос на получение списка запросов, созданных другими пользователями");
        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}
